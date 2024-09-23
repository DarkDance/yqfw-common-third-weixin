package cn.jzyunqi.common.third.weixin.mp.kefu.model;

import cn.jzyunqi.common.third.weixin.common.model.WeixinRsp;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author wiiyaya
 * @since 2024/9/23
 */
@Getter
@Setter
public class WxMpKfListRsp extends WeixinRsp {

    @JsonProperty("kf_list")
    private List<WxMpKfData> kfList;

    @JsonProperty("kf_online_list")
    private List<WxMpKfData> kfOnlineList;
}