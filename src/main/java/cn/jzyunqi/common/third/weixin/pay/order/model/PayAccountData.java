package cn.jzyunqi.common.third.weixin.pay.order.model;

import cn.jzyunqi.common.third.weixin.pay.order.enums.FundsAccount;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author wiiyaya
 * @since 2021/7/21.
 */
@Getter
@Setter
public class PayAccountData {
    /**
     * 出资账户类型
     */
    private FundsAccount account;

    /**
     * 出资金额
     */
    private Integer amount;

}
