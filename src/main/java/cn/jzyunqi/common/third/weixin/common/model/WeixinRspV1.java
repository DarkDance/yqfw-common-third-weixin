package cn.jzyunqi.common.third.weixin.common.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author wiiyaya
 * @since 2018/5/22.
 */
@Getter
@Setter
@ToString(callSuper = true)
public class WeixinRspV1 implements Serializable {
    @Serial
    private static final long serialVersionUID = -6792109548151994746L;

    /**
     * 错误代码
     */
    @JsonProperty("errcode")
    private String errorCode;

    /**
     * 错误信息
     */
    @JsonProperty("errmsg")
    private String errorMsg;
}
