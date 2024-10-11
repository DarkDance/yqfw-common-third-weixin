package cn.jzyunqi.common.third.weixin.mp.template.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wiiyaya
 * @since 2021/5/6.
 */
@Getter
public class WxMpTemplateMsgParam {

    /**
     * 接收者（用户）的 openid
     */
    @JsonProperty("touser")
    @Setter
    private String toUser;

    /**
     * 所需下发的订阅模板id
     */
    @Setter
    private String templateId;

    /**
     * 订阅通知消息专用
     */
    private Map<String, Map<String, String>> data;

    /**
     * 跳小程序所需数据，不需跳小程序可不用传该数据.
     */
    @JsonProperty("miniprogram")
    @Setter
    private MiniProgram miniProgram;

    /**
     * 防重入id。对于同一个openid + client_msg_id, 只发送一条消息,10分钟有效,超过10分钟不保证效果。若无防重入需求，可不填
     */
    @Setter
    private String clientMsgId;

    /**
     * 模板消息专用：模板跳转链接（海外账号没有跳转能力）
     */
    @Setter
    private String url;

    /**
     * 订阅消息专用：跳转网页时填写.
     */
    @Setter
    private String page;

    public void addData(String key, String value) {
        if (data == null) {
            data = new HashMap<>();
        }
        data.put(key, Map.of("value", value));
    }

    @Getter
    @Setter
    public static class MiniProgram {

        /**
         * 所需跳转到的小程序appid（该小程序appid必须与发模板消息的公众号是绑定关联关系，暂不支持小游戏）
         */
        @JsonProperty("appid")
        private String appId;

        /**
         * 所需跳转到小程序的具体页面路径，支持带参数,（示例index?foo=bar），要求该小程序已发布，暂不支持小游戏
         */
        @JsonProperty("pagepath")
        private String pagePath;
    }
}
