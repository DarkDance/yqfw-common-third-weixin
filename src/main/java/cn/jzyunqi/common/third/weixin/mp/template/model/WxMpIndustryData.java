package cn.jzyunqi.common.third.weixin.mp.template.model;

import cn.jzyunqi.common.third.weixin.common.model.WeixinRspV1;
import cn.jzyunqi.common.third.weixin.mp.template.enums.WxMpIndustryDeserializer;
import cn.jzyunqi.common.third.weixin.mp.template.enums.WxMpIndustryEnum;
import lombok.Getter;
import lombok.Setter;
import tools.jackson.databind.annotation.JsonDeserialize;

/**
 * @author wiiyaya
 * @since 2024/9/26
 */
@Getter
@Setter
public class WxMpIndustryData extends WeixinRspV1 {

    @JsonDeserialize(using = WxMpIndustryDeserializer.class)
    private WxMpIndustryEnum primaryIndustry;

    @JsonDeserialize(using = WxMpIndustryDeserializer.class)
    private WxMpIndustryEnum secondaryIndustry;

}
