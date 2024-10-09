package cn.jzyunqi.common.third.weixin.mp.card.model;

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
public class WxMpQrcodeParam {

    @JsonProperty("action_name")
    private String actionName;

    /**
     * 指定二维码的有效时间，范围是60 ~ 1800秒。不填默认为365天有效
     */
    @JsonProperty("expire_seconds")
    private Integer expireSeconds;

    @JsonProperty("action_info")
    private ActionInfo actionInfo;

    @Getter
    @Setter
    @ToString
    public static class ActionInfo {
        private CardInfo card;

        @JsonProperty("multiple_card")
        private MultipleCardInfo multipleCard;
    }

    @Getter
    @Setter
    @ToString
    public static class MultipleCardInfo {

        @JsonProperty("card_list")
        private List<CardInfo> cardList;
    }

    @Getter
    @Setter
    @ToString
    public static class CardInfo {

        /**
         * 卡券ID。
         */
        @JsonProperty("card_id")
        private String cardId;

        /**
         * 卡券Code码,use_custom_code字段为true的卡券必须填写，非自定义code和导入code模式的卡券不必填写。
         */
        private String code;

        /**
         * 指定领取者的openid，只有该用户能领取。bind_openid字段为true的卡券必须填写，非指定openid不必填写。
         */
        @JsonProperty("openid")
        private String openId;

        /**
         * 指定下发二维码，生成的二维码随机分配一个code，领取后不可再次扫描。填写true或false。默认false，注意填写该字段时，卡券须通过审核且库存不为0。
         */
        @JsonProperty("is_unique_code")
        private Boolean isUniqueCode;

        /**
         * outer_id字段升级版本，字符串类型，用户首次领卡时，会通过 领取事件推送 给商户； 对于会员卡的二维码，用户每次扫码打开会员卡后点击任何url，会将该值拼入url中，方便开发者定位扫码来源
         */
        @JsonProperty("outer_str")
        private String outerStr;
    }
}
