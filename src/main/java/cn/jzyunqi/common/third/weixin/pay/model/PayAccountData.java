package cn.jzyunqi.common.third.weixin.pay.model;

import cn.jzyunqi.common.third.weixin.mp.model.enums.FundsAccount;
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
public class PayAccountData implements Serializable {
    @Serial
    private static final long serialVersionUID = -8836971743503652816L;

    /**
     * 出资账户类型
     */
    private FundsAccount account;

    /**
     * 出资金额
     */
    private Integer amount;

}