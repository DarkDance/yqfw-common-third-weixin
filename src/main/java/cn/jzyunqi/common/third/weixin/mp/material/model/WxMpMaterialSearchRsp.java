package cn.jzyunqi.common.third.weixin.mp.material.model;

import cn.jzyunqi.common.third.weixin.common.model.WeixinRsp;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author wiiyaya
 * @since 2024/9/23
 */
@Getter
@Setter
public class WxMpMaterialSearchRsp extends WeixinRsp {

    @JsonProperty("total_count")
    private Integer totalCount;

    @JsonProperty("item_count")
    private Integer itemCount;

    private List<WxMpMaterialData> item;
}
