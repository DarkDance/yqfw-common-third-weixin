package cn.jzyunqi.common.third.weixin.pay.order.enums;

/**
 * @author shenhui
 * @since 2016/5/20.
 */
public enum TradeState {
    /**
     * 支付成功
     */
    SUCCESS,

    /**
     * 转入退款
     */
    REFUND,

    /**
     * 未支付
     */
    NOTPAY,

    /**
     * 已关闭
     */
    CLOSED,

    /**
     * 已撤销（刷卡支付）
     */
    REVOKED,

    /**
     * 用户支付中
     */
    USERPAYING,

    /**
     * 支付失败(其他原因，如银行返回失败)
     */
    PAYERROR,

    /**
     * 已接收，等待扣款
     */
    ACCEPT
}
