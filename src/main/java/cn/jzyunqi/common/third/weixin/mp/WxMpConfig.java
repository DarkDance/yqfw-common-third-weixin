package cn.jzyunqi.common.third.weixin.mp;

import lombok.Getter;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author wiiyaya
 * @since 2024/9/23
 */
@Getter
public class WxMpConfig {

    /**
     * 公众号唯一凭证
     */
    private final String appId;
    /**
     * 公众号唯一凭证密钥
     */
    private final String appSecret;

    /**
     * 客户端token key
     */
    private final String clientTokenKey;

    /**
     * js ticket key
     */
    private final String jsapiTicketKey;

    /**
     * js ticket key
     */
    private final String sdkTicketKey;

    /**
     * js ticket key
     */
    private final String wxCardTicketKey;

    public WxMpConfig(String appId, String appSecret) {
        this.appId = appId;
        this.appSecret = appSecret;
        this.clientTokenKey = "client_token:" + appId;
        this.jsapiTicketKey = "jsapi_ticket:" + appId;
        this.sdkTicketKey = "sdk_ticket:" + appId;
        this.wxCardTicketKey = "wx_card_ticket:" + appId;
    }
}
