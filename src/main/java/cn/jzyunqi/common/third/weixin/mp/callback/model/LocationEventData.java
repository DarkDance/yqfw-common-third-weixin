package cn.jzyunqi.common.third.weixin.mp.callback.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wiiyaya
 * @since 2024/10/9
 */
@Getter
@Setter
public class LocationEventData extends EventMsgData {
    private Double latitude;
    private Double longitude;
    private Double precision;
}
