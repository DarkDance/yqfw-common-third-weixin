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
public class WxMpMemberCardActiveParam {
    /**
     * 会员卡编号，由开发者填入，作为序列号显示在用户的卡包里。可与Code码保持等值。
     */
    @JsonProperty("membership_number")
    private String membershipNumber;
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
     * 商家自定义会员卡背景图，须先调用上传图片接口将背景图上传至CDN，否则报错。卡面设计请遵循微信会员卡自定义背景设计规范
     */
    @JsonProperty("background_pic_url")
    private String backgroundPicUrl;
    /**
     * 激活后的有效起始时间。若不填写默认以创建时的 data_info 为准。Unix时间戳格式。
     */
    @JsonProperty("activate_begin_time")
    private Integer activateBeginTime;
    /**
     * 激活后的有效截至时间。若不填写默认以创建时的 data_info 为准。Unix时间戳格式。
     */
    @JsonProperty("activate_end_time")
    private Integer activateEndTime;
    /**
     * 初始积分，不填为0。
     */
    @JsonProperty("init_bonus")
    private Integer initBonus;
    /**
     * 积分同步说明。
     */
    @JsonProperty("init_bonus_record")
    private String initBonusRecord;
    /**
     * 初始余额，不填为0。
     */
    @JsonProperty("init_balance")
    private Double initBalance;
    /**
     * 创建时字段custom_field1定义类型的初始值，限制为4个汉字，12字节。
     */
    @JsonProperty("init_custom_field_value1")
    private String initCustomFieldValue1;
    /**
     * 创建时字段custom_field2定义类型的初始值，限制为4个汉字，12字节。
     */
    @JsonProperty("init_custom_field_value2")
    private String initCustomFieldValue2;
    /**
     * 创建时字段custom_field3定义类型的初始值，限制为4个汉字，12字节。
     */
    @JsonProperty("init_custom_field_value3")
    private String initCustomFieldValue3;
}
