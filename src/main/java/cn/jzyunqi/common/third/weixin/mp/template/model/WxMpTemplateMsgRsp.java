package cn.jzyunqi.common.third.weixin.mp.template.model;

import cn.jzyunqi.common.third.weixin.common.model.WeixinRspV1;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wiiyaya
 * @since 2024/9/26
 */
@Getter
@Setter
public class WxMpTemplateMsgRsp extends WeixinRspV1 {

    @JsonProperty("msgid")
    private String msgId;
}
