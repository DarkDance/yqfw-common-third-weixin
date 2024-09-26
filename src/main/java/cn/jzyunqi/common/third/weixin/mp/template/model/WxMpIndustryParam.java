package cn.jzyunqi.common.third.weixin.mp.template.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wiiyaya
 * @since 2024/9/26
 */
@Getter
@Setter
public class WxMpIndustryParam {

    /**
     * 主行业编码
     */
    @JsonProperty("industry_id1")
    private Integer industryId1;

    /**
     * 副行业编码
     */
    @JsonProperty("industry_id2")
    private Integer industryId2;
}
