package com.geek.tao.bt10.xeknow;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.zip.GZIPInputStream;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import com.geek.common.exception.ServiceException;
import com.geek.tao.bt10.domain.XeknowReportEncryptedReq;

import jakarta.annotation.PostConstruct;

/**
 * 解密：RSA-OAEP(SHA-256) 解 AES 密钥 + AES-GCM 解 gzip 密文
 */
@Component
public class XeknowCryptoHelper {

    public static final String ALG_HYBRID = "rsa-oaep+aes-256-gcm+gzip";
    public static final String ALG_PLAINTEXT = "plaintext";

    private final XeknowProperties properties;
    private final ResourceLoader resourceLoader;

    private volatile PrivateKey privateKey;

    public XeknowCryptoHelper(XeknowProperties properties, ResourceLoader resourceLoader) {
        this.properties = properties;
        this.resourceLoader = resourceLoader;
    }

    @PostConstruct
    public void init() {
        try {
            String pem = resolvePem();
            if (StringUtils.hasText(pem)) {
                this.privateKey = parsePrivateKey(pem);
            }
        } catch (Exception e) {
            throw new IllegalStateException("加载 xeknow RSA 私钥失败: " + e.getMessage(), e);
        }
    }

    public String decryptToJson(XeknowReportEncryptedReq req) {
        if (req == null || !StringUtils.hasText(req.getCt())) {
            throw new ServiceException("上报密文 ct 不能为空");
        }
        String alg = req.getAlg() == null ? "" : req.getAlg().trim();
        try {
            if (ALG_PLAINTEXT.equalsIgnoreCase(alg)) {
                if (!properties.isAllowPlaintext()) {
                    throw new ServiceException("未开启 plaintext 上报");
                }
                return decodePlaintext(req.getCt());
            }
            if (!ALG_HYBRID.equalsIgnoreCase(alg) && StringUtils.hasText(alg)) {
                throw new ServiceException("不支持的 alg: " + alg);
            }
            if (privateKey == null) {
                throw new ServiceException("服务端未配置解密私钥");
            }
            if (!StringUtils.hasText(req.getEk()) || !StringUtils.hasText(req.getIv())) {
                throw new ServiceException("ek/iv 不能为空");
            }
            byte[] aesKey = rsaOaepDecrypt(Base64.getDecoder().decode(req.getEk()));
            byte[] iv = Base64.getDecoder().decode(req.getIv());
            byte[] cipherBytes = Base64.getDecoder().decode(req.getCt());
            byte[] gzipBytes = aesGcmDecrypt(aesKey, iv, cipherBytes);
            return gunzipToString(gzipBytes);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("上报解密失败: " + e.getMessage());
        }
    }

    private String resolvePem() throws IOException {
        if (StringUtils.hasText(properties.getPrivateKeyPem())) {
            return properties.getPrivateKeyPem();
        }
        String loc = properties.getPrivateKeyLocation();
        if (!StringUtils.hasText(loc)) {
            return null;
        }
        Resource resource = resourceLoader.getResource(loc);
        if (!resource.exists()) {
            return null;
        }
        try (InputStream in = resource.getInputStream()) {
            return StreamUtils.copyToString(in, StandardCharsets.UTF_8);
        }
    }

    private static PrivateKey parsePrivateKey(String pem) throws Exception {
        String normalized = pem
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replace("-----BEGIN RSA PRIVATE KEY-----", "")
                .replace("-----END RSA PRIVATE KEY-----", "")
                .replaceAll("\\s", "");
        byte[] decoded = Base64.getDecoder().decode(normalized);
        // PKCS#1 → 尝试包装为 PKCS#8
        if (pem.contains("BEGIN RSA PRIVATE KEY")) {
            decoded = wrapPkcs1ToPkcs8(decoded);
        }
        return KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
    }

    /** 简易 PKCS#1 RSA 私钥包装为 PKCS#8（openssl genrsa 默认输出） */
    private static byte[] wrapPkcs1ToPkcs8(byte[] pkcs1) {
        // SEQUENCE { version=0, algorithm=rsaEncryption, privateKey OCTET STRING }
        byte[] algId = new byte[] {
                0x30, 0x0d,
                0x06, 0x09, 0x2a, (byte) 0x86, 0x48, (byte) 0x86, (byte) 0xf7, 0x0d, 0x01, 0x01, 0x01,
                0x05, 0x00
        };
        byte[] version = new byte[] { 0x02, 0x01, 0x00 };
        byte[] octet = derOctetString(pkcs1);
        byte[] body = concat(version, algId, octet);
        return derSequence(body);
    }

    private static byte[] derOctetString(byte[] content) {
        return concat(new byte[] { 0x04 }, derLength(content.length), content);
    }

    private static byte[] derSequence(byte[] content) {
        return concat(new byte[] { 0x30 }, derLength(content.length), content);
    }

    private static byte[] derLength(int len) {
        if (len < 0x80) {
            return new byte[] { (byte) len };
        }
        if (len <= 0xff) {
            return new byte[] { (byte) 0x81, (byte) len };
        }
        return new byte[] { (byte) 0x82, (byte) (len >> 8), (byte) len };
    }

    private static byte[] concat(byte[]... parts) {
        int n = 0;
        for (byte[] p : parts) {
            n += p.length;
        }
        byte[] out = new byte[n];
        int i = 0;
        for (byte[] p : parts) {
            System.arraycopy(p, 0, out, i, p.length);
            i += p.length;
        }
        return out;
    }

    private byte[] rsaOaepDecrypt(byte[] encryptedKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        OAEPParameterSpec spec = new OAEPParameterSpec(
                "SHA-256", "MGF1", MGF1ParameterSpec.SHA256, PSource.PSpecified.DEFAULT);
        cipher.init(Cipher.DECRYPT_MODE, privateKey, spec);
        return cipher.doFinal(encryptedKey);
    }

    private static byte[] aesGcmDecrypt(byte[] key, byte[] iv, byte[] cipherBytes) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"), new GCMParameterSpec(128, iv));
        return cipher.doFinal(cipherBytes);
    }

    private static String gunzipToString(byte[] gzipBytes) throws IOException {
        try (GZIPInputStream gin = new GZIPInputStream(new ByteArrayInputStream(gzipBytes))) {
            return StreamUtils.copyToString(gin, StandardCharsets.UTF_8);
        }
    }

    private static String decodePlaintext(String ct) throws IOException {
        // 优先按 Base64(gzip) 解析，失败则当 UTF-8 JSON
        try {
            byte[] raw = Base64.getDecoder().decode(ct);
            if (raw.length >= 2 && (raw[0] == (byte) 0x1f) && (raw[1] == (byte) 0x8b)) {
                return gunzipToString(raw);
            }
            return new String(raw, StandardCharsets.UTF_8);
        } catch (IllegalArgumentException e) {
            return ct;
        }
    }
}
