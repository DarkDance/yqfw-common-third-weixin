package cn.jzyunqi.common.third.weixin.pay.order.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author wiiyaya
 * @since 2024/10/13
 */
@Getter
@Setter
@ToString
public class UnifiedAppOrderData {
    /**
     * 微信开放平台审核通过的移动应用AppID 。
     */
    @JsonProperty("appid")
    private String appId;

    /**
     * 请填写商户号mchid对应的值。
     */
    private String partnerId;

    /**
     * 微信返回的支付交易会话ID，该值有效期为2小时。
     */
    private String prepayId;

    /**
     * 暂填写固定值Sign=WXPay
     */
    @JsonProperty("package")
    private String packageValue;

    /**
     * 随机字符串，不长于32位。推荐随机数生成算法。
     */
    private String nonceStr;

    /**
     * 时间戳，标准北京时间，时区为东八区，自1970年1月1日 0点0分0秒以来的秒数。注意：部分系统取到的值为毫秒级，需要转换成秒(10位数字)。
     */
    private Long timeStamp;

    /**
     * 签名，使用字段AppID、timeStamp、nonceStr、prepayid计算得出的签名值 注意：取值RSA格式
     */
    private String sign;
}
