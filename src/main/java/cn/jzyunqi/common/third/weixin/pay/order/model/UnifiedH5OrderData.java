package cn.jzyunqi.common.third.weixin.pay.order.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author wiiyaya
 * @since 2025/4/22
 */
@Getter
@Setter
@ToString
public class UnifiedH5OrderData {

    /**
     * 申请支付单号
     */
    private String applyPayNo;

    /**
     * 二维码URL / H5支付链接
     */
    private String url;
}
