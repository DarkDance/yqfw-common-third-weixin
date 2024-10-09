package cn.jzyunqi.common.third.weixin.mp.card.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author wiiyaya
 * @since 2024/10/9
 */
@Getter
@Setter
@ToString
public class WxMpMemberCardUpdateParam {

    /**
     * 领取会员卡用户获得的code
     */
    private String code;
    /**
     * 卡券ID,自定义code卡券必填
     */
    @JsonProperty("card_id")
    private String cardId;
    /**
     * 支持商家激活时针对单个会员卡分配自定义的会员卡背景
     */
    @JsonProperty("background_pic_url")
    private String backgroundPicUrl;
    /**
     * 需要设置的积分全量值，传入的数值会直接显示
     */
    private Integer bonus;

    /**
     * 本次积分变动值，传负数代表减少
     */
    @JsonProperty("add_bonus")
    private Integer addBonus;
    /**
     * 商家自定义积分消耗记录，不超过14个汉字
     */
    @JsonProperty("record_bonus")
    private String recordBonus;
    /**
     * 需要设置的余额全量值，传入的数值会直接显示在卡面
     */
    private Double balance;
    /**
     * 本次余额变动值，传负数代表减少
     */
    @JsonProperty("add_balance")
    private Double addBalance;
    /**
     * 商家自定义金额消耗记录，不超过14个汉字。
     */
    @JsonProperty("record_balance")
    private String recordBalance;

    /**
     * 创建时字段custom_field定义类型的最新数值，限制为4个汉字，12字节。
     */
    @JsonProperty("custom_field_value1")
    private String customFieldValue1;
    @JsonProperty("custom_field_value2")
    private String customFieldValue2;
    @JsonProperty("custom_field_value3")
    private String customFieldValue3;

    @JsonProperty("notify_optional")
    private NotifyOptional notifyOptional;

    @Getter
    @Setter
    @ToString
    public static class NotifyOptional {
        /**
         * 积分变动时是否触发系统模板消息，默认为true
         */
        @JsonProperty("is_notify_bonus")
        private Boolean isNotifyBonus;

        /**
         * 余额变动时是否触发系统模板消息，默认为true
         */
        @JsonProperty("is_notify_balance")
        private Boolean isNotifyBalance;

        /**
         * 自定义group1变动时是否触发系统模板消息，默认为false。（2、3同理）
         */
        @JsonProperty("is_notify_custom_field1")
        private Boolean isNotifyCustomField1;

        @JsonProperty("is_notify_custom_field2")
        private Boolean isNotifyCustomField2;

        @JsonProperty("is_notify_custom_field3")
        private Boolean isNotifyCustomField3;
    }
}
