package cn.jzyunqi.common.third.weixin.pay.model.request;

import cn.jzyunqi.common.third.weixin.mp.model.enums.FundsAccount;
import cn.jzyunqi.common.third.weixin.pay.model.PayAmountData;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author wiiyaya
 * @since 2021/7/21.
 */
@Getter
@Setter
public class RefundOrderParam implements Serializable {
    private static final long serialVersionUID = -5936281128286155210L;

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
