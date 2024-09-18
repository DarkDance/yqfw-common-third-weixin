package cn.jzyunqi.common.third.weixin.open.model.response;

import cn.jzyunqi.common.third.weixin.common.response.WeixinRsp;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @author wiiyaya
 * @since 2024/9/18
 */
public class OpenUserInfoRsp extends WeixinRsp {
    /**
     * 微信unionId
     */
    @JsonProperty("unionid")
    private String unionId;

    /**
     * 微信openId
     */
    @JsonProperty("openid")
    private String openId;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 用户头像
     */
    @JsonProperty("headimgurl")
    private String headImgUrl;

    /**
     * 性别，1为男性，2为女性
     */
    private String sex;

    /**
     * 省份
     */
    private String province;

    /**
     * 国家，如中国为CN
     */
    private String country;

    /**
     * 城市
     */
    private String city;

    /**
     * 用户特权信息
     */
    private List<String> privilege;
}
