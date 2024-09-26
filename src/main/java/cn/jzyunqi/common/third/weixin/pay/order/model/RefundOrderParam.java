package cn.jzyunqi.common.third.weixin.pay.order.model;

import cn.jzyunqi.common.third.weixin.pay.order.enums.FundsAccount;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wiiyaya
 * @since 2021/7/21.
 */
@Getter
@Setter
public class RefundOrderParam {

    /**
     * 微信支付订单号
     */
    private String transactionId;

    /**
     * 商户订单号
     */
    private String outTradeNo;

    /**
     * 商户退款单号
     */
    private String outRefundNo;

    /**
     * 退款原因
     */
    private String reason;

    /**
     * 退款结果回调url
     */
    private String notifyUrl;

    /**
     * 退款资金来源
     */
    private FundsAccount fundsAccount;

    /**
     * 金额信息
     */
    private PayAmountData amount = new PayAmountData();
}
