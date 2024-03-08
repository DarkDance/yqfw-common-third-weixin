package cn.jzyunqi.common.third.weixin.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

/**
 * @author wiiyaya
 * @date 2021/5/6.
 */
@Getter
@Setter
public class TmpMsgParam implements Serializable {
    private static final long serialVersionUID = 9085121034679247706L;

    /**
     * 用户openid，可以是小程序的也可以是公众号的
     */
    @JsonProperty("touser")
    private String toUser;

    /**
     * 小程序模板ID
     */
    @JsonProperty("template_id")
    private String templateId;

    /**
     * 小程序页面路径
     */
    private String page;

    /**
     * 小程序模板数据
     */
    private Map<String, Map<String, String>> data;

    /**
     * 跳转小程序类型developer为开发版；trial为体验版；formal为正式版；默认为正式版
     */
    @JsonProperty("miniprogram_state")
    private String miniProgramState;

    /**
     * 支持语言类型zh_CN(简体中文)、en_US(英文)、zh_HK(繁体中文)、zh_TW(繁体中文)，默认为zh_CN
     */
    private String lang;
}
