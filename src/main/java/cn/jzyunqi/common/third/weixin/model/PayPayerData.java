package cn.jzyunqi.common.third.weixin.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author wiiyaya
 * @date 2021/7/21.
 */
@Getter
@Setter
public class PayPayerData implements Serializable {
    @Serial
    private static final long serialVersionUID = 1341366278701373383L;

    /**
     * 微信用户标识
     */
    @JsonProperty("openid")
    private String openId;
}
