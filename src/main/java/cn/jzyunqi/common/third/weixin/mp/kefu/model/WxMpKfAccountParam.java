package cn.jzyunqi.common.third.weixin.mp.kefu.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wiiyaya
 * @since 2024/9/20
 */
@Getter
@Setter
public class WxMpKfAccountParam {
    /**
     * 完整客服账号，格式为：账号前缀@公众号微信号
     */
    private String kfAccount;

    /**
     * 客服昵称，最长6个汉字或12个英文字符
     */
    private String nickname;

    /**
     * 客服账号登录密码，格式为密码明文的32位加密MD5值。该密码仅用于在公众平台官网的多客服功能中使用，若不使用多客服功能，则不必设置密码
     */
    private String password;

    /**
     * 接收绑定邀请的客服微信号
     */
    private String inviteWx;
}
