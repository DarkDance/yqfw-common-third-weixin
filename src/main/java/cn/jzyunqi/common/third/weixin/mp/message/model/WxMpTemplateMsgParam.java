package cn.jzyunqi.common.third.weixin.mp.message.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

/**
 * @author wiiyaya
 * @since 2021/5/6.
 */
@Getter
@Setter
public class WxMpTemplateMsgParam {

    /**
     * 接收者（用户）的 openid
     */
    @JsonProperty("touser")
    private String toUser;

    /**
     * 所需下发的订阅模板id
     */
    @JsonProperty("template_id")
    private String templateId;

    /**
     * 跳转网页时填写.
     */
    private String page;

    /**
     * 跳小程序所需数据，不需跳小程序可不用传该数据.
     */
    @JsonProperty("miniprogram")
    private MiniProgram miniProgram;

    /**
     * 订阅通知消息专用
     */
    private Map<String, String> data;

    @Getter
    @Setter
    public static class MiniProgram {

        private String appid;

        @JsonProperty("pagepath")
        private String pagePath;
    }
}
