package cn.jzyunqi.common.third.weixin.model.enums;

/**
 * @author wiiyaya
 * @date 2021/7/21.
 */
public enum FundsAccount {

    /**
     * 未结算资金
     */
    UNSETTLED,

    /**
     * 可用余额
     */
    AVAILABLE,

    /**
     * 不可用余额
     */
    UNAVAILABLE,

    /**
     * 运营户
     */
    OPERATION,

    /**
     * 基本账户（含可用余额和不可用余额）
     */
    BASIC,
}
