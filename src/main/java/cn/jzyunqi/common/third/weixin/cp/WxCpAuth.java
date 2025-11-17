package cn.jzyunqi.common.third.weixin.cp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author wiiyaya
 * @since 2025/11/17
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WxCpAuth {

    /**
     * 企业ID
     */
    private String corpId;

    /**
     * 企业应用ID
     */
    private String agentId;

    /**
     * 企业密码
     */
    private String corpSecret;

    /**
     * 消息token
     */
    private String verificationToken;

    /**
     * 消息体加密密钥
     */
    private String encryptKey;
}
