package cn.jzyunqi.common.third.weixin.model.callback;

import cn.jzyunqi.common.third.weixin.model.callback.item.PayResultData;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author wiiyaya
 * @since 2021/7/21.
 */
@Getter
@Setter
public class PayResultCb implements Serializable {
    @Serial
    private static final long serialVersionUID = 2984109498800330498L;

    private String id;

    @JsonProperty("create_time")
    private String createTime;

    @JsonProperty("event_type")
    private String eventType;

    @JsonProperty("resource_type")
    private String resourceType;

    private String summary;

    private PayResultData resource;
}
