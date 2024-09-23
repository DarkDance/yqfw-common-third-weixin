package cn.jzyunqi.common.third.weixin.mp.callback.model;

import cn.jzyunqi.common.third.weixin.mp.callback.enums.EventType;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wiiyaya
 * @since 2024/3/9
 */
@Getter
@Setter
public class EventMsgData extends BaseMsgData{

    private EventType event;
}
