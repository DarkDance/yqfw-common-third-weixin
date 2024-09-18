package cn.jzyunqi.common.third.weixin.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author wiiyaya
 * @since 2021/7/20.
 */
@Getter
@Setter
@ToString
public class UnifiedOrderV3Rsp implements Serializable {
    @Serial
    private static final long serialVersionUID = -1336500637573348603L;

    /**
     * 申请流水号
     */
    private String applyPayNo;

    /**
     * 随机字符串
     */
    private String nonceStr;

    /**
     * 时间戳
     */
    private String timeStamp;

    /**
     * 扩展字段
     */
    @JsonProperty("package")
    private String weixinPackage;

    /**
     * 签名方式
     */
    private String signType;

    /**
     * 签名
     */
    private String paySign;
}
