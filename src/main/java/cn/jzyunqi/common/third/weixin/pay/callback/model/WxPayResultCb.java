package cn.jzyunqi.common.third.weixin.pay.callback.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author wiiyaya
 * @since 2021/7/21.
 */
@Getter
@Setter
@ToString
public class WxPayResultCb {

    private String id;

    @JsonProperty("create_time")
    private String createTime;

    @JsonProperty("event_type")
    private String eventType;

    @JsonProperty("resource_type")
    private String resourceType;

    private String summary;

    private WxPayResultData resource;
}
