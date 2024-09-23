package cn.jzyunqi.common.third.weixin.mp.material.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wiiyaya
 * @since 2024/9/23
 */
@Getter
@Setter
public class WxMpMaterialData {
    private String media_id;
    private String update_time;
    private String name;
    private String url;
    private WxMpMaterialNewsData content;
}
