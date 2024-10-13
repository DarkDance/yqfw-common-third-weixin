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
public class GoodsData {
    /**
     * <pre>
     * 字段名：商户侧商品编码
     * 变量名：merchant_goods_id
     * 是否必填：是
     * 类型：string[1,32]
     * 描述：
     *  由半角的大小写字母、数字、中划线、下划线中的一种或几种组成。
     *  示例值：商品编码
     * </pre>
     */
    private String merchantGoodsId;

    /**
     * <pre>
     * 字段名：微信侧商品编码
     * 变量名：wechatpay_goods_id
     * 是否必填：否
     * 类型：string[1,32]
     * 描述：
     *  微信支付定义的统一商品编号（没有可不传）
     *  示例值：1001
     * </pre>
     */
    private String wechatpayGoodsId;

    /**
     * <pre>
     * 字段名：商品名称
     * 变量名：goods_name
     * 是否必填：否
     * 类型：string[1,256]
     * 描述：
     *  商品的实际名称
     *  示例值：iPhoneX 256G
     * </pre>
     */
    private String goodsName;

    /**
     * <pre>
     * 字段名：商品单价
     * 变量名：unit_price
     * 是否必填：是
     * 类型：int
     * 描述：
     *  商品单价，单位为分
     *  示例值：828800
     * </pre>
     */
    private Integer unitPrice;

    /**
     * <pre>
     * 字段名：支付商品数量
     * 变量名：quantity
     * 是否必填：否(支付时必填)
     * 类型：int
     * 描述：
     *  用户购买的数量
     *  示例值：1
     * </pre>
     */
    private Integer quantity;

    /**
     * <pre>
     * 字段名：商品退款金额
     * 变量名：refund_amount
     * 是否必填：否(退款时必填)
     * 类型：int
     * 描述：
     *  商品退款金额，单位为分。
     *  示例值：528800
     * </pre>
     */
    private Integer refundAmount;

    /**
     * <pre>
     * 字段名：商品退货数量
     * 变量名：refund_quantity
     * 是否必填：否(退款时必填)
     * 类型：int
     * 描述：
     *  单品的退款数量。
     *  示例值：1
     * </pre>
     */
    private Integer refundQuantity;
}
