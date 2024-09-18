package cn.jzyunqi.common.third.weixin.model.callback.item;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author wiiyaya
 * @since 2024/3/9
 */
@Getter
@Setter
public class BaseMsgData {
    /**
     * 开发者微信号
     */
    private String toUserName;

    /**
     * 发送方账号（一个OpenID）
     */
    private String fromUserName;

    /**
     * 消息创建时间 （整型）
     */
    private LocalDateTime createTime;

    /**
     * 消息id，64位整型
     */
    private Long msgId;
}
