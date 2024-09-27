package cn.jzyunqi.common.third.weixin.mp;

/**
 * @author wiiyaya
 * @since 2024/9/23
 */
public interface WxMpClientConfig {

    /**
     * 公众号唯一凭证
     */
    String getAppId();

    /**
     * 公众号唯一凭证密钥
     */
    String getAppSecret();

    /**
     * 消息token
     */
    String getMsgToken();

    /**
     * 消息体加密密钥
     */
    String getMsgEncodingAesKey();
}
