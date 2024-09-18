package cn.jzyunqi.common.third.weixin.pay.model.enums;

/**
 * @author wiiyaya
 * @since 2021/7/21.
 */
public enum RefundChannel {

    /**
     * 原路退款
     */
    ORIGINAL,

    /**
     * 退回到余额
     */
    BALANCE,

    /**
     * 原账户异常退到其他余额账户
     */
    OTHER_BALANCE,

    /**
     * 原银行卡异常退到其他银行卡
     */
    OTHER_BANKCARD,
}
