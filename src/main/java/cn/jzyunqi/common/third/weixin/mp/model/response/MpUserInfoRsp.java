package cn.jzyunqi.common.third.weixin.mp.model.response;

import cn.jzyunqi.common.third.weixin.common.response.WeixinRsp;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.util.List;

/**
 * @author wiiyaya
 * @since 2018/5/22.
 */
@Getter
@Setter
public class MpUserInfoRsp extends WeixinRsp {
    @Serial
    private static final long serialVersionUID = -1078218095463872809L;

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
    @JsonProperty("subscribe_time")
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
    @JsonProperty("subscribe_scene")
    private String subscribeScene;

    /**
     * 二维码扫码场景
     */
    @JsonProperty("qr_scene")
    private Integer qrScene;

    /**
     * 二维码扫码场景描述
     */
    @JsonProperty("qr_scene_str")
    private String qrSceneStr;
}
