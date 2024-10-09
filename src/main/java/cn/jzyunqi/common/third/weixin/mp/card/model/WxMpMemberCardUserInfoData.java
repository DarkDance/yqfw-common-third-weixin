package cn.jzyunqi.common.third.weixin.mp.card.model;

import cn.jzyunqi.common.third.weixin.common.model.WeixinRspV1;
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
public class WxMpMemberCardUserInfoData extends WeixinRspV1 {

    private String openId;

    private String nickname;

    @JsonProperty("membership_number")
    private String membershipNumber;

    private Integer bonus;

    private Double balance;

    private String sex;

    @JsonProperty("user_info")
    private UserInfo userInfo;

    @JsonProperty("user_card_status")
    private String userCardStatus;

    @JsonProperty("has_active")
    private Boolean hasActive;

    @Getter
    @Setter
    @ToString
    public static class UserInfo {
        @JsonProperty("common_field_list")
        private NameValues[] commonFieldList;
        @JsonProperty("custom_field_list")
        private NameValues[] customFieldList;
    }

    @Getter
    @Setter
    @ToString
    public static class NameValues {
        private String name;

        private String value;

        @JsonProperty("value_list")
        private String[] valueList;
    }
}
