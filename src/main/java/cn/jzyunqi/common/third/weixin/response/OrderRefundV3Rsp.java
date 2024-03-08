package cn.jzyunqi.common.third.weixin.response;

import cn.jzyunqi.common.third.weixin.enums.FundsAccount;
import cn.jzyunqi.common.third.weixin.enums.RefundChannel;
import cn.jzyunqi.common.third.weixin.enums.RefundStatus;
import cn.jzyunqi.common.third.weixin.model.PayAmountData;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * @author wiiyaya
 * @date 2018/5/30.
 */
@Getter
@Setter
public class OrderRefundV3Rsp implements Serializable {
    private static final long serialVersionUID = 9001001048933315054L;
    /**
     * 微信退款单号
     */
    public String refundId;

    /**
     * 商户退款单号
     */
    private String outRefundNo;

    /**
     * 微信支付订单号
     */
    private String transactionId;

    /**
     * 订单号
     */
    private String outTradeNo;

    /**
     * 退款渠道
     */
    private RefundChannel channel;

    /**
     * 退款入账账户
     */
    private String userReceivedAccount;

    /**
     * 退款成功时间
     */
    private OffsetDateTime successTime;

    /**
     * 退款受理时间
     */
    private OffsetDateTime createTime;

    /**
     * 退款状态
     */
    private RefundStatus status;

    /**
     * 资金账户
     */
    private FundsAccount fundsAccount;

    /**
     * 订单金额
     */
    private PayAmountData amount = new PayAmountData();

}
