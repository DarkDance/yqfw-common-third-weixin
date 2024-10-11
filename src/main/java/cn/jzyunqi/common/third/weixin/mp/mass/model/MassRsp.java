package cn.jzyunqi.common.third.weixin.mp.mass.model;

import cn.jzyunqi.common.third.weixin.common.model.WeixinRspV1;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;

/**
 * @author wiiyaya
 * @since 2018/8/25.
 */
@Getter
@Setter
@ToString
public class MassRsp extends WeixinRspV1 {

    /**
     * 消息发送任务的ID
     */
    private String msgId;

    /**
     * 消息的数据ID
     */
    private String msgDataId;
}
