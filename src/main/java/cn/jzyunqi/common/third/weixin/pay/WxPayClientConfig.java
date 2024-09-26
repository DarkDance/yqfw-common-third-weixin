package cn.jzyunqi.common.third.weixin.pay;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wiiyaya
 * @since 2024/9/25
 */
@Getter
@AllArgsConstructor
public class WxPayClientConfig {

    /**
     * 应用唯一标识
     */
    private final String appId;

    /**
     * 支付回调URL
     */
    private final String payCallbackUrl;

    /**
     * 退款回调URL
     */
    private final String refundCallbackUrl;

    /**
     * 支付商户号
     */
    private final String merchantId;

    /**
     * 支付商户证书私钥
     */
    private final String merchantPrivateKey;

    /**
     * 支付商户证书序列号
     */
    private final String merchantSerialNumber;

    /**
     * 支付商户AES key
     */
    private final String merchantAesKey;
}
