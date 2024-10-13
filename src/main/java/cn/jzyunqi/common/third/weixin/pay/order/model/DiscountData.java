package cn.jzyunqi.common.third.weixin.pay.order.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author wiiyaya
 * @since 2024/10/13
 */
@Getter
@Setter
@ToString
public class DiscountData {
    /**
     * <pre>
     * 字段名：订单原价
     * 变量名：cost_price
     * 是否必填：否
     * 类型：int
     * 描述：
     *  1、商户侧一张小票订单可能被分多次支付，订单原价用于记录整张小票的交易金额。
     *  2、当订单原价与支付金额不相等，则不享受优惠。
     *  3、该字段主要用于防止同一张小票分多次支付，以享受多次优惠的情况，正常支付订单不必上传此参数。
     *  示例值：608800
     * </pre>
     */
    private Integer costPrice;

    /**
     * <pre>
     * 字段名：商品小票ID
     * 变量名：invoice_id
     * 是否必填：否
     * 类型：string[1,32]
     * 描述：
     *  商品小票ID
     *  示例值：微信123
     * </pre>
     */
    private String invoiceId;

    /**
     * <pre>
     * 字段名：单品列表
     * 变量名：goods_detail
     * 是否必填：否
     * 类型：array
     * 描述：
     *  单品列表信息
     *  条目个数限制：【1，6000】
     * </pre>
     */
    private List<GoodsData> goodsDetails;
}
