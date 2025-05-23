package cn.jzyunqi.common.third.weixin.pay.order.model;

import cn.jzyunqi.common.third.weixin.common.model.WeixinRspV1;
import cn.jzyunqi.common.third.weixin.pay.order.enums.TradeState;
import cn.jzyunqi.common.third.weixin.common.enums.TradeType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * @author wiiyaya
 * @since 2018/5/30.
 */
@Getter
@Setter
@ToString
public class OrderData extends WeixinRspV1 {

    /**
     * 应用ID
     */
    @JsonProperty("appid")
    private String appId;

    /**
     * 直连商户号
     */
    @JsonProperty("mchid")
    private String mchId;

    /**
     * 商户订单号
     */
    private String outTradeNo;

    /**
     * 微信支付订单号
     */
    private String transactionId;

    /**
     * 交易类型
     */
    private TradeType tradeType;

    /**
     * 交易状态
     */
    private TradeState tradeState;

    /**
     * 交易状态描述
     */
    private String tradeStateDesc;

    /**
     * 付款银行
     */
    private String bankType;

    /**
     * 附加数据
     */
    private String attach;

    /**
     * 支付完成时间
     */
    private OffsetDateTime successTime;

    /**
     * 订单金额
     */
    private PayAmountData amount;

    /**
     * 支付者
     */
    private PayPayerData payer;

    /**
     * 支付金额(非接口返回，手动计算)
     */
    @JsonIgnore
    private BigDecimal actualPayAmount;

    /**
     * 查询返回字符串(非接口返回，手动拼接)
     */
    @JsonIgnore
    private String responseStr;

}
