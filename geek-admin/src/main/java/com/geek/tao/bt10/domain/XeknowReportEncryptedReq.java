package com.geek.tao.bt10.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 油猴脚本上报密文体
 */
@Data
@Schema(title = "xeknow 加密上报请求")
public class XeknowReportEncryptedReq {

    @Schema(title = "协议版本")
    private Integer v;

    @Schema(title = "算法标识")
    private String alg;

    @Schema(title = "密钥版本")
    private String kid;

    @Schema(title = "RSA 加密后的 AES 密钥 Base64")
    private String ek;

    @Schema(title = "AES-GCM IV Base64")
    private String iv;

    @Schema(title = "密文 Base64")
    private String ct;

    @Schema(title = "客户端时间戳秒")
    private Long ts;
}
