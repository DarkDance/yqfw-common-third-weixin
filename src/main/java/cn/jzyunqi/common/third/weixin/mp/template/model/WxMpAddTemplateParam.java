package cn.jzyunqi.common.third.weixin.mp.template.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author wiiyaya
 * @since 2024/9/26
 */
@Getter
@Setter
public class WxMpAddTemplateParam {

    /**
     * 模板库中模板的编号，有“TM**”和“OPENTMTM**”等形式,对于类目模板，为纯数字ID
     */
    private String templateIdShort;

    /**
     * 选用的类目模板的关键词,按顺序传入,如果为空，或者关键词不在模板库中，会返回40246错误码
     */
    private List<String> keywordNameList;
}
