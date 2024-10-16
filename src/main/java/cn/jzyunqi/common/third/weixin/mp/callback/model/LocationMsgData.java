package cn.jzyunqi.common.third.weixin.mp.callback.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wiiyaya
 * @since 2024/3/9
 */
@Getter
@Setter
public class LocationMsgData extends BaseMsgData {
    private Double locationX;
    private Double locationY;
    private Integer scale;
    private String label;
}
