package cn.jzyunqi.common.third.weixin.cp.callback.model;

import cn.jzyunqi.common.third.weixin.mp.callback.enums.MsgType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wiiyaya
 * @since 2025/12/2
 */
@Getter
@Setter
public class BaseChatData {

    /**
     * 消息id，消息的唯一标识，企业可以使用此字段进行消息去重
     */
    @JsonProperty("msgid")
    private String msgId;

    /**
     * 消息动作，目前有send(发送消息)/recall(撤回消息)/switch(切换企业日志)三种类型
     */
    private String action;

    /**
     * 消息发送方id。同一企业内容为userid，非相同企业为external_userid。消息如果是机器人发出，也为external_userid
     */
    private String from;

    /**
     * 消息接收方列表，可能是多个，同一个企业内容为userid，非相同企业为external_userid
     */
    @JsonProperty("tolist")
    private String[] toList;

    /**
     * 群聊消息的群id。如果是单聊则为空
     */
    @JsonProperty("roomid")
    private String roomId;

    /**
     * 消息发送时间戳，utc时间，ms单位
     */
    @JsonProperty("msgtime")
    private Long msgTime;

    /**
     * 消息类型
     */
    @JsonProperty("msgtype")
    private MsgType msgType;
}
