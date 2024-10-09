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
public class BaseInfoData {

    /**
     * 卡券的商户logo,建议像素为300*300.
     */
    @JsonProperty("logo_url")
    private String logoUrl;

    /**
     * Code展示类型.
     * "CODE_TYPE_TEXT" 文本 "CODE_TYPE_BARCODE" 一维码 "CODE_TYPE_QRCODE" 二维码 "CODE_TYPE_ONLY_QRCODE" 仅显示二维码 "CODE_TYPE_ONLY_BARCODE" 仅显示一维码 "CODE_TYPE_NONE" 不显示任何码型
     */
    @JsonProperty("code_type")
    private String codeType = "CODE_TYPE_QRCODE";

    /**
     * 支付功能结构体,swipe_card结构.
     */
    @JsonProperty("pay_info")
    private PayInfoData payInfo;

    /**
     * 是否设置该会员卡中部的按钮同时支持微信支付刷卡和会员卡二维码.
     */
    @JsonProperty("is_pay_and_qrcode")
    private boolean isPayAndQrcode;

    /**
     * 商户名字,字数上限为12个汉字.
     */
    @JsonProperty("brand_name")
    private String brandName;

    /**
     * 卡券名,字数上限为9个汉字 (建议涵盖卡券属性、服务及金额).
     */
    @JsonProperty("title")
    private String title;

    /**
     * 券颜色,按色彩规范标注填写Color010-Color100.
     */
    @JsonProperty("color")
    private String color;

    /**
     * 卡券使用提醒,字数上限为16个汉字.
     */
    @JsonProperty("notice")
    private String notice;

    /**
     * 卡券使用说明,字数上限为1024个汉字.
     */
    @JsonProperty("description")
    private String description;

    /**
     * 商品信息.
     */
    @JsonProperty("sku")
    private Sku sku;

    /**
     * 使用日期,有效期的信息.
     */
    @JsonProperty("date_info")
    private DateInfo dateInfo;

    /**
     * 是否自定义Code码,填写true或false.
     * 默认为false 通常自有优惠码系统的开发者选择自定义Code码，详情见 是否自定义code
     */
    @JsonProperty("use_custom_code")
    private boolean useCustomCode;

    /**
     * 是否指定用户领取,填写true或false。默认为false.
     */
    @JsonProperty("bind_openid")
    private boolean bindOpenid;

    /**
     * 客服电话.
     */
    @JsonProperty("service_phone")
    private String servicePhone;

    /**
     * 门店位置ID,调用 POI门店管理接口 获取门店位置ID.
     */
    @JsonProperty("location_id_list")
    private List<String> locationIdList;

    /**
     * 会员卡是否支持全部门店,填写后商户门店更新时会自动同步至卡券.
     */
    @JsonProperty("use_all_locations")
    private boolean useAllLocations = true;

    /**
     * 卡券中部居中的按钮,仅在卡券激活后且可用状态 时显示.
     */
    @JsonProperty("center_title")
    private String centerTitle;

    /**
     * 显示在入口下方的提示语,仅在卡券激活后且可用状态时显示.
     */
    @JsonProperty("center_sub_title")
    private String centerSubTitle;

    /**
     * 顶部居中的url,仅在卡券激活后且可用状态时显示.
     */
    @JsonProperty("center_url")
    private String centerUrl;

    /**
     * 自定义跳转外链的入口名字.
     */
    @JsonProperty("custom_url_name")
    private String customUrlName;

    /**
     * 自定义跳转的URL.
     */
    @JsonProperty("custom_url")
    private String customUrl;

    /**
     * 显示在入口右侧的提示语.
     */
    @JsonProperty("custom_url_sub_title")
    private String customUrlSubTitle;

    /**
     * 营销场景的自定义入口名称.
     */
    @JsonProperty("promotion_url_name")
    private String promotionUrlName;

    /**
     * 入口跳转外链的地址链接.
     */
    @JsonProperty("promotion_url")
    private String promotionUrl;

    /**
     * 显示在营销入口右侧的提示语.
     */
    @JsonProperty("promotion_url_sub_title")
    private String promotionUrlSubTitle;

    /**
     * 每人可领券的数量限制,建议会员卡每人限领一张.
     */
    @JsonProperty("get_limit")
    private Integer getLimit = 1;

    /**
     * 每人可核销的数量限制,不填写默认为50.
     */
    @JsonProperty("use_limit")
    private Integer useLimit = 50;

    /**
     * 卡券领取页面是否可分享,默认为true.
     */
    @JsonProperty("can_share")
    private boolean canShare;

    /**
     * 卡券是否可转赠,默认为true.
     */
    @JsonProperty("can_give_friend")
    private boolean canGiveFriend;

    /**
     * 用户点击进入会员卡时推送事件.
     * 填写true为用户点击进入会员卡时推送事件，默认为false。详情见 进入会员卡事件推送
     */
    @JsonProperty("need_push_on_view")
    private boolean needPushOnView;

    /**
     * 微信小程序开放功能 小程序&卡券打通部分新增8个字段 https://mp.weixin.qq.com/cgi-bin/announce?action=getannouncement&key=1490190158&version=1&lang=zh_CN&platform=2
     * 自定义使用入口跳转小程序的user_name，格式为原始id+@app
     */
    @JsonProperty("custom_app_brand_user_name")
    private String customAppBrandUserName;
    /**
     * 自定义使用入口小程序页面地址
     */
    @JsonProperty("custom_app_brand_pass")
    private String customAppBrandPass;
    /**
     * 小程序的user_name
     */
    @JsonProperty("center_app_brand_user_name")
    private String centerAppBrandUserName;
    /**
     * 自定义居中使用入口小程序页面地址
     */
    @JsonProperty("center_app_brand_pass")
    private String centerAppBrandPass;
    /**
     * 小程序的user_name
     */
    @JsonProperty("promotion_app_brand_user_name")
    private String promotionAppBrandUserName;
    /**
     * 自定义营销入口小程序页面地址
     */
    @JsonProperty("promotion_app_brand_pass")
    private String promotionAppBrandPass;

    /**
     * 小程序的user_name,
     */
    @JsonProperty("activate_app_brand_user_name")
    private String activateAppBrandUserName;
    /**
     * 激活小程序页面地址
     */
    @JsonProperty("activate_app_brand_pass")
    private String activateAppBrandPass;

    /**
     * https://developers.weixin.qq.com/doc/offiaccount/Cards_and_Offer/Managing_Coupons_Vouchers_and_Cards.html#2
     * “CARD_STATUS_NOT_VERIFY”,待审核 ；
     * “CARD_STATUS_VERIFY_FAIL”,审核失败；
     * “CARD_STATUS_VERIFY_OK”，通过审核；
     * “CARD_STATUS_DELETE”，卡券被商户删除；
     * “CARD_STATUS_DISPATCH”，在公众平台投放过的卡券 ；
     */
    private String status;

    @Getter
    @Setter
    @ToString
    public static class PayInfoData {

        /**
         * 刷卡功能
         */
        @JsonProperty("swipe_card")
        private SwipeCard swipeCard;
    }

    @Getter
    @Setter
    @ToString
    public static class SwipeCard {

        /**
         * 是否设置该会员卡支持拉出微信支付刷卡界面
         */
        @JsonProperty("is_swipe_card")
        private Boolean isSwipeCard;
    }

    @Getter
    @Setter
    @ToString
    public static class Sku {

        /**
         * 卡券库存的数量，不支持填写0，上限为100000000
         */
        @JsonProperty("quantity")
        private Integer quantity;

        /**
         * 卡券全部库存的数量，上限为100000000。
         * https://developers.weixin.qq.com/doc/offiaccount/Cards_and_Offer/Managing_Coupons_Vouchers_and_Cards.html#4
         */
        @JsonProperty("total_quantity")
        private Integer totalQuantity;
    }

    @Getter
    @Setter
    @ToString
    public static class DateInfo {
        /**
         * 使用时间的类型.
         * 支持固定时长有效类型 固定日期有效类型 永久有效类型：DATE_TYPE_FIX_TERM_RANGE、DATE_TYPE_FIX_TERM 、DATE_TYPE_PERMANENT
         */
        @JsonProperty("type")
        private String type = "DATE_TYPE_PERMANENT";

        /**
         * 起用时间.
         * type为DATE_TYPE_FIX_TIME_RANGE时专用， 表示起用时间。从1970年1月1日00:00:00至起用时间的秒数 （ 东八区时间,UTC+8，单位为秒 ）
         */
        @JsonProperty("begin_timestamp")
        private Long beginTimestamp;

        /**
         * 结束时间.
         * type为DATE_TYPE_FIX_TERM_RANGE时专用，表示结束时间 （ 东八区时间,UTC+8，单位为秒 ）
         */
        @JsonProperty("end_timestamp")
        private Long endTimestamp;

        /**
         * 自领取后多少天内有效.
         * type为DATE_TYPE_FIX_TERM时专用，表示自领取后多少天内有效，领取后当天有效填写0（单位为天）
         */
        @JsonProperty("fixed_term")
        private Integer fixedTerm;

        /**
         * 自领取后多少天开始生效.
         * type为DATE_TYPE_FIX_TERM时专用，表示自领取后多少天开始生效。（单位为天）
         */
        @JsonProperty("fixed_begin_term")
        private Integer fixedBeginTerm;
    }
}
