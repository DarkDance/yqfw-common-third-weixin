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
public class WxMpKfMsgListRsp extends WeixinRsp {

    @JsonProperty("recordlist")
    private List<WxMpKfMsgData> records;

    @JsonProperty("number")
    private Integer number;

    @JsonProperty("msgid")
    private Long msgId;
}