package cn.jzyunqi.common.third.weixin.mp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author wiiyaya
 * @since 2018/9/21.
 */
@Getter
@Setter
@ToString
public class UserData implements Serializable {
    @Serial
    private static final long serialVersionUID = 605838017330601965L;

    /**
     * 微信unionId
     */
    private String unionId;

    /**
     * 微信openId
     */
    private String openId;

    /**
     * 用户头像
     */
    private String avatarUrl;

    /**
     * 昵称
     */
    @JsonProperty("nickName")
    private String nickname;

    /**
     * 性别，1为男性，2为女性
     */
    private String gender;

    /**
     * 城市
     */
    private String city;

    /**
     * 省份
     */
    private String province;

    /**
     * 国家，如中国为CN
     */
    private String country;
}
