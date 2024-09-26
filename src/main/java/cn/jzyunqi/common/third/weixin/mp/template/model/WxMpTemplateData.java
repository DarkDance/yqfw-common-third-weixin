package cn.jzyunqi.common.third.weixin.mp.template.model;

import cn.jzyunqi.common.third.weixin.common.model.WeixinRspV1;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wiiyaya
 * @since 2024/9/26
 */
@Getter
@Setter
public class WxMpTemplateData extends WeixinRspV1 {

    /**
     * 模板ID
     */
    @JsonProperty("template_id")
    private String templateId;

    /**
     * 模板标题
     */
    @JsonProperty("title")
    private String title;

    /**
     * 模板所属行业的一级行业
     */
    @JsonProperty("primary_industry")
    private String primaryIndustry;

    /**
     * 模板所属行业的二级行业
     */
    @JsonProperty("deputy_industry")
    private String deputyIndustry;

    /**
     * 模板内容
     */
    @JsonProperty("content")
    private String content;

    /**
     * 模板示例
     */
    @JsonProperty("example")
    private String example;
}
