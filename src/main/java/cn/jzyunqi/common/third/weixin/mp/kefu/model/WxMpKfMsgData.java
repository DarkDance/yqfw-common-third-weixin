package cn.jzyunqi.common.third.weixin.mp.kefu.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wiiyaya
 * @since 2024/9/23
 */
@Getter
@Setter
public class WxMpKfMsgData {

    /**
     * 完整客服帐号，格式为：帐号前缀@公众号微信号
     */
    private String worker;

    /**
     * 用户标识
     */
    @JsonProperty("openid")
    private String openId;

    /**
     * 操作码，2002（客服发送信息），2003（客服接收消息）
     */
    @JsonProperty("opercode")
    private Integer operateCode;

    /**
     * 聊天记录
     */
    private String text;

    /**
     * 操作时间，unix时间戳
     */
    private Long time;
}
