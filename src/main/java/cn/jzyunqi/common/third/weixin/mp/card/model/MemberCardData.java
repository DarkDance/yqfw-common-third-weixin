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
public class MemberCardData extends WxMpCardData {

    /**
     * 会员卡背景图.
     */
    @JsonProperty("background_pic_url")
    private String backgroundPicUrl;

    /**
     * 特权说明.
     */
    @JsonProperty("prerogative")
    private String prerogative;

    /**
     * 自动激活.
     */
    @JsonProperty("auto_activate")
    private boolean autoActivate;

    /**
     * 显示积分.
     */
    @JsonProperty("supply_bonus")
    private boolean supplyBonus;

    /**
     * 查看积分外链,设置跳转外链查看积分详情。仅适用于积分无法通过激活接口同步的情况下使用该字段.
     */
    @JsonProperty("bonus_url")
    private String bonusUrl;

    /**
     * 支持储值.
     */
    @JsonProperty("supply_balance")
    private boolean supplyBalance;

    /**
     * 余额外链,仅适用于余额无法通过激活接口同步的情况下使用该字段.
     */
    @JsonProperty("balance_url")
    private String balanceUrl;

    /**
     * 自定义会员类目1,会员卡激活后显示.
     */
    @JsonProperty("custom_field1")
    private CustomField customField1;

    /**
     * 自定义会员类目2.
     */
    @JsonProperty("custom_field2")
    private CustomField customField2;

    /**
     * 自定义会员类目3.
     */
    @JsonProperty("custom_field3")
    private CustomField customField3;

    /**
     * 积分清零规则.
     */
    @JsonProperty("bonus_cleared")
    private String bonusCleared;

    /**
     * 积分规则.
     */
    @JsonProperty("bonus_rules")
    private String bonusRules;

    /**
     * 储值规则.
     */
    @JsonProperty("balance_rules")
    private String balanceRules;

    /**
     * 激活会员卡的url.
     */
    @JsonProperty("activate_url")
    private String activateUrl;

    /**
     * 激活会原卡url对应的小程序user_name，仅可跳转该公众号绑定的小程序.
     */
    @JsonProperty("activate_app_brand_user_name")
    private String activateAppBrandUserName;

    /**
     * 激活会原卡url对应的小程序path.
     */
    @JsonProperty("activate_app_brand_pass")
    private String activateAppBrandPass;

    /**
     * 自定义会员信息类目，会员卡激活后显示.
     */
    @JsonProperty("custom_cell1")
    private CustomCell customCell1;

    /**
     * 自定义会员信息类目，会员卡激活后显示.
     */
    @JsonProperty("custom_cell2")
    private CustomCell customCell2;


    /**
     * 自定义会员信息类目，会员卡激活后显示.
     */
    @JsonProperty("custom_cell3")
    private CustomCell customCell3;

    /**
     * 积分规则,JSON结构积分规则.
     */
    @JsonProperty("bonus_rule")
    private BonusRule bonusRule;

    /**
     * 折扣,该会员卡享受的折扣优惠,填10就是九折.
     */
    private Integer discount;

    /**
     * 是否支持一键激活 ，填true或false.
     */
    @JsonProperty("wx_activate")
    private boolean wxActivate;

    /**
     * 是否支持跳转型一键激活，填true或false.
     */
    @JsonProperty("wx_activate_after_submit")
    private boolean wxActivateAfterSubmit;

    /**
     * 跳转型一键激活跳转的地址链接，请填写http:// 或者https://开头的链接.
     */
    @JsonProperty("wx_activate_after_submit_url")
    private String wxActivateAfterSubmitUrl;

    /**
     * 参照https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1499332673_Unm7V卡券内跳转小程序
     */
    /**
     * 积分信息类目对应的小程序 user_name，格式为原始id+@app
     */
    @JsonProperty("bonus_app_brand_user_name")
    private String bonusAppBrandUserName;
    /**
     * 积分入口小程序的页面路径
     */
    @JsonProperty("bonus_app_brand_pass")
    private String bonusAppBrandPass;
    /**
     * 余额信息类目对应的小程序 user_name，格式为原始id+@app
     */
    @JsonProperty("balance_app_brand_user_name")
    private String balanceAppBrandUserName;
    /**
     * 余额入口小程序的页面路径
     */
    @JsonProperty("balance_app_brand_pass")
    private String balanceAppBrandPass;

    @Getter
    @Setter
    @ToString
    public static class CustomField {
        /**
         * 半自定义名称,当开发者变更这类类目信息的value值时 可以选择触发系统模板消息通知用户。 FIELD_NAME_TYPE_LEVEL 等级 FIELD_NAME_TYPE_COUPON 优惠券 FIELD_NAME_TYPE_STAMP 印花 FIELD_NAME_TYPE_DISCOUNT 折扣 FIELD_NAME_TYPE_ACHIEVEMEN 成就 FIELD_NAME_TYPE_MILEAGE 里程 FIELD_NAME_TYPE_SET_POINTS 集点 FIELD_NAME_TYPE_TIMS 次数
         */
        @JsonProperty("name_type")
        private String nameType;

        /**
         * 自定义名称,当开发者变更这类类目信息的value值时 不会触发系统模板消息通知用户
         */
        private String name;

        /**
         * 点击类目跳转外链url
         */
        private String url;

        /**
         * 参考https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1499332673_Unm7V卡券内跳转小程序参数说明：会员卡顶部的信息类目字段，包含以下两个字段
         * 自定义信息类目小程序user_name，格式为原始id+@app
         */
        @JsonProperty("app_brand_user_name")
        private String appBrandUserName;
        /**
         * 自定义信息类目小程序的页面路径
         */
        @JsonProperty("app_brand_pass")
        private String appBrandPass;
    }

    @Getter
    @Setter
    @ToString
    public static class CustomCell {
        /**
         * 入口名称.
         */
        private String name;

        /**
         * 入口右侧提示语,6个汉字内.
         */
        private String tips;

        /**
         * 入口跳转链接.
         */
        private String url;

        /**
         * 参考https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1499332673_Unm7V卡券内跳转小程序参数说明：会员卡自定义入口，包含以下两个字段
         * 自定义入口小程序user_name，格式为原始id+@app.
         */
        @JsonProperty("app_brand_user_name")
        private String appBrandUserName;
        /**
         * 自定义入口小程序的页面路径.
         */
        @JsonProperty("app_brand_pass")
        private String appBrandPass;
    }

    @Getter
    @Setter
    @ToString
    public static class BonusRule {
        /**
         * 消费金额,以分为单位.
         */
        @JsonProperty("cost_money_unit")
        private Integer costMoneyUnit;

        /**
         * 对应增加的积分.
         */
        @JsonProperty("increase_bonus")
        private Integer increaseBonus;

        /**
         * 用户单次可获取的积分上限.
         */
        @JsonProperty("max_increase_bonus")
        private Integer maxIncreaseBonus;

        /**
         * 初始设置积分.
         */
        @JsonProperty("init_increase_bonus")
        private Integer initIncreaseBonus;

        /**
         * 每使用积分.
         */
        @JsonProperty("cost_bonus_unit")
        private Integer costBonusUnit;

        /**
         * 抵扣xx元,这里以分为单位）.
         */
        @JsonProperty("reduce_money")
        private Integer reduceMoney;

        /**
         * 抵扣条件,满xx元（这里以分为单位）可用.
         */
        @JsonProperty("least_money_to_use_bonus")
        private Integer leastMoneyToUseBonus;

        /**
         * 抵扣条件,单笔最多使用xx积分.
         */
        @JsonProperty("max_reduce_bonus")
        private Integer maxReduceBonus;
    }
}
