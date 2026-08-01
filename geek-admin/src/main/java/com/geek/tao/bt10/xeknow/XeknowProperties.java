package com.geek.tao.bt10.xeknow;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * xeknow 采集上报配置
 */
@Data
@ConfigurationProperties(prefix = "geek.xeknow")
public class XeknowProperties {

    /** 密钥版本，与脚本 kid 对齐 */
    private String kid = "default";

    /**
     * RSA 私钥 PEM（PKCS#8 或 PKCS#1）。
     * 也可用 privateKeyLocation 从 classpath/文件加载。
     */
    private String privateKeyPem;

    /** 如 classpath:xeknow/private.pem */
    private String privateKeyLocation = "classpath:xeknow/private.pem";

    /**
     * 开发联调：允许 alg=plaintext 时 ct 为明文/Base64(gzip) JSON（生产务必 false）
     */
    private boolean allowPlaintext = false;

    /** 缺号延期天数 */
    private int phoneRetryDays = 3;
}
