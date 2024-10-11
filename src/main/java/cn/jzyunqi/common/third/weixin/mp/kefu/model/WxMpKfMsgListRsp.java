package cn.jzyunqi.common.third.weixin.mp.kefu.model;

import cn.jzyunqi.common.third.weixin.common.model.WeixinRspV1;
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
public class WxMpKfMsgListRsp extends WeixinRspV1 {

    @JsonProperty("recordlist")
    private List<WxMpKfMsgData> records;

    private Integer number;

    @JsonProperty("msgid")
    private Long msgId;
}
