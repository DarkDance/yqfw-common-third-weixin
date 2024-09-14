package cn.jzyunqi.common.third.weixin.model.callback.item;

import cn.jzyunqi.common.third.weixin.model.enums.EventType;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wiiyaya
 * @date 2024/3/9
 */
@Getter
@Setter
public class EventMsgData extends BaseMsgData{

    private EventType event;
}
