package cn.jzyunqi.common.third.weixin.mp.user.model;

import cn.jzyunqi.common.third.weixin.common.model.WeixinRspV1;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.util.List;

/**
 * @author wiiyaya
 * @since 2018/5/22.
 */
@Getter
@Setter
@ToString
public class MpUserData {

    /**
     * 是否关注
     */
    private Boolean subscribe;

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
     * 语言
     */
    private String language;

    /**
     * 用户关注时间，为时间戳
     */
    private Integer subscribeTime;

    /**
     * 公众号对用户的备注
     */
    private String remark;

    /**
     * 用户所在的分组ID
     */
    private Integer groupId;

    /**
     * 用户被打上的标签
     */
    @JsonProperty("tagid_list")
    private List<String> tagIdList;

    /**
     * 用户关注的渠道
     */
    private String subscribeScene;

    /**
     * 二维码扫码场景
     */
    private Integer qrScene;

    /**
     * 二维码扫码场景描述
     */
    private String qrSceneStr;
}
