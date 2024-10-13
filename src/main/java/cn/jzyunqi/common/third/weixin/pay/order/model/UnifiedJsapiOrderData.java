package cn.jzyunqi.common.third.weixin.pay.order.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author wiiyaya
 * @since 2021/7/20.
 */
@Getter
@Setter
@ToString
public class UnifiedJsapiOrderData {

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
