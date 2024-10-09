package cn.jzyunqi.common.third.weixin.mp.card.model;

import cn.jzyunqi.common.third.weixin.mp.card.enums.LandingSceneType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author wiiyaya
 * @since 2024/10/9
 */
@Getter
@Setter
@ToString
public class WxMpLandingPageParam {

    /**
     * 页面的banner图片链接，须调用，建议尺寸为640*300。
     */
    private String banner;

    /**
     * 页面的title
     */
    @JsonProperty("page_title")
    private String title;

    /**
     * 页面是否可以分享,填入true/false
     */
    @JsonProperty("can_share")
    private Boolean canShare;

    /**
     * 投放页面的场景值
     */
    private LandingSceneType scene;

    @JsonProperty("card_list")
    private List<CardInfo> cardList;

    @Getter
    @Setter
    @ToString
    public static class CardInfo {
        /**
         * 所要在页面投放的card_id
         */
        @JsonProperty("card_id")
        private String cardId;

        /**
         * 缩略图url
         */
        @JsonProperty("thumb_url")
        private String thumbUrl;
    }
}
