package cn.jzyunqi.common.third.weixin.pay.model.response;

import cn.jzyunqi.common.third.weixin.pay.model.enums.TradeState;
import cn.jzyunqi.common.third.weixin.common.enums.TradeType;
import cn.jzyunqi.common.third.weixin.pay.model.PayAmountData;
import cn.jzyunqi.common.third.weixin.pay.model.PayPayerData;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * @author wiiyaya
 * @since 2018/5/30.
 */
@Getter
@Setter
public class OrderQueryV3Rsp implements Serializable {
    @Serial
    private static final long serialVersionUID = 4010260078103420851L;

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
    private PayAmountData amount = new PayAmountData();

    /**
     * 支付者
     */
    private PayPayerData payer = new PayPayerData();

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
