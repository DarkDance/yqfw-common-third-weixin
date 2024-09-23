package cn.jzyunqi.common.third.weixin.mp.kefu.model;

import cn.jzyunqi.common.third.weixin.mp.kefu.enums.InviteStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wiiyaya
 * @since 2024/9/23
 */
@Getter
@Setter
public class WxMpKfData {
    /**
     * 完整客服账号，格式为：账号前缀@公众号微信号
     */
    @JsonProperty("kf_account")
    private String account;

    /**
     * 客服头像地址
     */
    @JsonProperty("kf_headimgurl")
    private String headImgUrl;

    /**
     * 客服工号
     */
    @JsonProperty("kf_id")
    private String id;

    /**
     * 客服昵称
     */
    @JsonProperty("kf_nick")
    private String nick;

    /**
     * 如果客服帐号已绑定了客服人员微信号，则此处显示微信号
     */
    @JsonProperty("kf_wx")
    private String wxAccount;

    /**
     * 如果客服帐号尚未绑定微信号，但是已经发起了一个绑定邀请，则此处显示绑定邀请的微信号
     */
    @JsonProperty("invite_wx")
    private String inviteWx;

    /**
     * 如果客服帐号尚未绑定微信号，但是已经发起过一个绑定邀请，则此处显示为邀请的过期时间，为unix 时间戳
     */
    @JsonProperty("invite_expire_time")
    private Long inviteExpireTime;

    /**
     * 邀请的状态
     */
    @JsonProperty("invite_status")
    private InviteStatus inviteStatus;

    /**
     * 客服在线状态，目前为：1、web 在线
     */
    @JsonProperty("status")
    private Integer status;

    /**
     * 客服当前正在接待的会话数
     */
    @JsonProperty("accepted_case")
    private Integer acceptedCase;
}
