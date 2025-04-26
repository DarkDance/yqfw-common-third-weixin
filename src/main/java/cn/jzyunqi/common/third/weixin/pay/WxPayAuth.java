package cn.jzyunqi.common.third.weixin.pay;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author wiiyaya
 * @since 2025/4/26
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WxPayAuth {

    /**
     * 与merchantId关联的公众号AppID、开放平台AppID、小程序AppID
     */
    private String wxAppId;

    /**
     * 支付商户号
     */
    private String merchantId;

    /**
     * 支付商户证书私钥
     */
    private String merchantPrivateKey;

    /**
     * 支付商户证书序列号
     */
    private String merchantSerialNumber;

    /**
     * 支付商户AES key
     */
    private String merchantAesKey;

    /**
     * 支付回调URL
     */
    private String payCallbackUrl;

    /**
     * 退款回调URL
     */
    private String refundCallbackUrl;
}
