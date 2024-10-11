package cn.jzyunqi.common.third.weixin.pay.order.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author wiiyaya
 * @since 2021/7/21.
 */
@Getter
@Setter
public class PayAmountData {
    /**
     * 订单总金额，单位为分
     */
    private Integer total;

    /**
     * CNY：人民币，境内商户号仅支持人民币
     */
    private String currency;

    /**
     * 退款金额
     */
    private Integer refund;

    /**
     * 用户支付金额，单位为分
     */
    private Integer payerTotal;

    /**
     * 用户退款金额
     */
    private Integer payerRefund;

    /**
     * 用户支付币种
     */
    private String payerCurrency;

    /**
     * 应结订单金额
     */
    private Integer settlementTotal;

    /**
     * 应结退款金额(去掉非充值代金券退款金额后的退款金额)
     */
    private Integer settlementRefund;

    /**
     * 退款出资账户及金额
     */
    private List<PayAccountData> from;
}
