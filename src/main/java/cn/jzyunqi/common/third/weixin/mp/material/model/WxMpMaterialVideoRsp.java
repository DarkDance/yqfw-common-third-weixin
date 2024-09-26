package cn.jzyunqi.common.third.weixin.mp.material.model;

import cn.jzyunqi.common.third.weixin.common.model.WeixinRspV1;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wiiyaya
 * @since 2024/9/23
 */
@Getter
@Setter
public class WxMpMaterialVideoRsp extends WeixinRspV1 {

    private String title;

    private String description;

    @JsonProperty("down_url")
    private String downUrl;
}
