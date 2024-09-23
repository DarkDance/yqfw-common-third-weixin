package cn.jzyunqi.common.third.weixin.mp.material.model;

import cn.jzyunqi.common.third.weixin.mp.material.enums.MaterialType;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wiiyaya
 * @since 2024/9/23
 */
@Getter
@Setter
public class WxMpMaterialSearchParam {
    private MaterialType type;
    private Integer offset;
    private Integer count;
}
