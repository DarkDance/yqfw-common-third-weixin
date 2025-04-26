package cn.jzyunqi.common.third.weixin.mp;

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
public class WxMpAuth {
    /**
     * 公众号唯一凭证
     */
    private String appId;

    /**
     * 公众号唯一凭证密钥
     */
    private String appSecret;

    /**
     * 消息token
     */
    private String verificationToken;//getMsgToken();

    /**
     * 消息体加密密钥
     */
    private String encryptKey;//getMsgEncodingAesKey();
}
