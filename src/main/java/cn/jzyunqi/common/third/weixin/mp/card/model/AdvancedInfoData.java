package cn.jzyunqi.common.third.weixin.mp.card.model;

import cn.jzyunqi.common.third.weixin.mp.card.enums.ServiceType;
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
public class AdvancedInfoData {
    /**
     * 使用门槛（条件）.
     * 若不填写使用条件则在券面拼写 ：无最低消费限制，全场通用，不限品类；并在使用说明显示： 可与其他优惠共享
     */
    @JsonProperty("use_condition")
    private UseCondition useCondition;

    /**
     * 封面摘要.
     */
    @JsonProperty("abstract")
    private Abstract abstractInfo;

    /**
     * 图文列表.
     * 显示在详情内页 ，优惠券券开发者须至少传入 一组图文列表
     */
    @JsonProperty("text_image_list")
    private List<TextImageList> textImageList;

    /**
     * 商家服务类型.
     * 数组类型:BIZ_SERVICE_DELIVER 外卖服务； BIZ_SERVICE_FREE_PARK 停车位； BIZ_SERVICE_WITH_PET 可带宠物； BIZ_SERVICE_FREE_WIFI 免费wifi， 可多选
     */
    @JsonProperty("business_service")
    private List<ServiceType> businessServiceList;

    /**
     * 使用时段限制.
     */
    @JsonProperty("time_limit")
    private List<TimeLimit> timeLimits;

    /**
     * 是否可以分享朋友.
     */
    @JsonProperty("share_friends")
    private Boolean shareFriends;

    @Getter
    @Setter
    @ToString
    public static class UseCondition {
        /**
         * 指定可用的商品类目,仅用于代金券类型 ，填入后将在券面拼写适用于xxx
         */
        @JsonProperty("accept_category")
        private String acceptCategory;

        /**
         * 指定不可用的商品类目,仅用于代金券类型 ，填入后将在券面拼写不适用于xxxx
         */
        @JsonProperty("reject_category")
        private String rejectCategory;

        /**
         * 满减门槛字段,可用于兑换券和代金券 ，填入后将在全面拼写消费满xx元可用
         */
        @JsonProperty("least_cost")
        private Integer leastCost;

        /**
         * 购买xx可用类型门槛,仅用于兑换 ，填入后自动拼写购买xxx可用
         */
        @JsonProperty("object_use_for")
        private String objectUseFor;

        /**
         * 不可以与其他类型共享门槛,填写false时系统将在使用须知里 拼写“不可与其他优惠共享”， 填写true时系统将在使用须知里 拼写“可与其他优惠共享”， 默认为true
         */
        @JsonProperty("can_use_with_other_discount")
        private boolean canUseWithOtherDiscount;
    }

    @Getter
    @Setter
    @ToString
    public static class Abstract {
        /**
         * 摘要.
         */
        @JsonProperty("abstract")
        private String abstractInfo;

        /**
         * 封面图片列表.
         * 仅支持填入一 个封面图片链接， 上传图片接口 上传获取图片获得链接，填写 非CDN链接会报错，并在此填入。 建议图片尺寸像素850*350
         */
        @JsonProperty("icon_url_list")
        private String iconUrlList;
    }

    @Getter
    @Setter
    @ToString
    public static class TextImageList {
        /**
         * 图片链接,必须调用 上传图片接口 上传图片获得链接，并在此填入， 否则报错
         */
        @JsonProperty("image_url")
        private String imageUrl;

        /**
         * 图文描述.
         */
        @JsonProperty("text")
        private String text;
    }

    @Getter
    @Setter
    @ToString
    public static class TimeLimit {
        /**
         * 限制类型枚举值,支持填入 MONDAY 周一 TUESDAY 周二 WEDNESDAY 周三 THURSDAY 周四 FRIDAY 周五 SATURDAY 周六 SUNDAY 周日 此处只控制显示， 不控制实际使用逻辑，不填默认不显示
         */
        private String type;

        /**
         * 起始时间（小时）,当前type类型下的起始时间（小时） ，如当前结构体内填写了MONDAY， 此处填写了10，则此处表示周一 10:00可用
         */
        @JsonProperty("begin_hour")
        private Integer beginHour;

        /**
         * 起始时间（分钟）,如当前结构体内填写了MONDAY， begin_hour填写10，此处填写了59， 则此处表示周一 10:59可用
         */
        @JsonProperty("begin_minute")
        private Integer beginMinute;

        /**
         * 结束时间（小时）,如当前结构体内填写了MONDAY， 此处填写了20， 则此处表示周一 10:00-20:00可用
         */
        @JsonProperty("end_hour")
        private Integer endHour;

        /**
         * 结束时间（分钟）,如当前结构体内填写了MONDAY， begin_hour填写10，此处填写了59， 则此处表示周一 10:59-00:59可用
         */
        @JsonProperty("end_minute")
        private Integer endMinute;
    }
}
