package cn.jzyunqi.common.third.weixin.pay;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Blob;

/**
 * @author wiiyaya
 * @since 2024/9/25
 */
public interface WxPayClientConfig {

    /**
     * 支付回调URL
     */
    String getPayCallbackUrl();

    /**
     * 退款回调URL
     */
    String getRefundCallbackUrl();

    /**
     * 支付商户号
     */
    String getMerchantId();

    /**
     * 支付商户证书私钥
     */
    String getMerchantPrivateKey();

    /**
     * 支付商户证书序列号
     */
    String getMerchantSerialNumber();

    /**
     * 支付商户AES key
     */
    String getMerchantAesKey();
}
