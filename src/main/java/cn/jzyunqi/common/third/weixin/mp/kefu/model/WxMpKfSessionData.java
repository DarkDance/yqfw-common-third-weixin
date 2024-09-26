package cn.jzyunqi.common.third.weixin.mp.kefu.model;

import cn.jzyunqi.common.third.weixin.common.model.WeixinRspV1;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wiiyaya
 * @since 2024/9/23
 */
@Getter
@Setter
public class WxMpKfSessionData extends WeixinRspV1 {

    /**
     * 正在接待的客服，为空表示没有人在接待
     */
    @JsonProperty("kf_account")
    private String kfAccount;

    /**
     * 会话接入的时间，UNIX时间戳
     */
    @JsonProperty("createtime")
    private long createTime;

    /**
     * 粉丝的最后一条消息的时间，UNIX时间戳
     */
    @JsonProperty("latest_time")
    private long latestTime;

    /**
     * 客户openid
     */
    @JsonProperty("openid")
    private String openid;

}
