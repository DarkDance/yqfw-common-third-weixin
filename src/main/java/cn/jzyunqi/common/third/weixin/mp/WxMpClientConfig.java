package cn.jzyunqi.common.third.weixin.mp;

/**
 * @author wiiyaya
 * @since 2024/9/23
 */
public interface WxMpClientConfig {

    String getAppId();

    String getAppSecret();

    String getMsgToken();

    String getMsgEncodingAesKey();
}
