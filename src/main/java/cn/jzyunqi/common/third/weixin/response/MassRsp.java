package cn.jzyunqi.common.third.weixin.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wiiyaya
 * @date 2018/8/25.
 */
@Getter
@Setter
public class MassRsp extends WeixinOpenRsp {
    private static final long serialVersionUID = 6485880153599316122L;

    /**
     * 消息发送任务的ID
     */
    @JsonProperty("msg_id")
    private String msgId;

    /**
     * 消息的数据ID
     */
    @JsonProperty("msg_data_id")
    private String msgDataId;
}
