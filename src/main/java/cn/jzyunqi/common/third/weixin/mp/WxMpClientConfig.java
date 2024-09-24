package cn.jzyunqi.common.third.weixin.mp;

import cn.jzyunqi.common.utils.DigestUtilPlus;
import lombok.Getter;
import org.apache.commons.codec.binary.Base64;

import java.util.Arrays;

/**
 * @author wiiyaya
 * @since 2024/9/23
 */
@Getter
public class WxMpClientConfig {

    /**
     * 公众号唯一凭证
     */
    private final String appId;
    /**
     * 公众号唯一凭证密钥
     */
    private final String appSecret;

    /**
     * 消息tokrn
     */
    private final String msgToken;

    /**
     * 用户信息同步中转页面
     */
    private final String userSyncUrl;

    /**
     * 消息体加密密钥
     */
    private final byte[] msgEncodingAesKey;

    /**
     * 消息体加密向量
     */
    private final byte[] msgEncodingAesIv;

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

    public WxMpClientConfig(String appId, String appSecret, String msgToken, String msgEncodingAesKey, String userSyncUrl) {
        this.appId = appId;
        this.appSecret = appSecret;
        this.msgToken = msgToken;
        this.msgEncodingAesKey = DigestUtilPlus.Base64.decodeBase64(msgEncodingAesKey);
        this.userSyncUrl = userSyncUrl;

        this.msgEncodingAesIv = Arrays.copyOfRange(Base64.decodeBase64(this.msgEncodingAesKey), 0, 16);
        this.clientTokenKey = "client_token:" + appId;
        this.jsapiTicketKey = "jsapi_ticket:" + appId;
        this.sdkTicketKey = "sdk_ticket:" + appId;
        this.wxCardTicketKey = "wx_card_ticket:" + appId;
    }
}
