package cn.jzyunqi.common.third.weixin.pay.order.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author wiiyaya
 * @since 2024/10/13
 */
@Getter
@Setter
@ToString
public class SettleInfoData {

    /**
     * <pre>
     * 字段名：是否指定分账
     * 变量名：profit_sharing
     * 是否必填：否
     * 类型：bool
     * 描述：
     *  是否指定分账，枚举值
     *  true：是
     *  false：否
     *  示例值：true
     * </pre>
     */
    private Boolean profitSharing;
}
