package cn.jzyunqi.common.third.weixin.common.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author wiiyaya
 * @since 2018/5/22.
 */
@Getter
@Setter
public class WeixinRspV2<T> extends WeixinRsp {

    private Integer count;

    private T data;

}
