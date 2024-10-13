package cn.jzyunqi.common.third.weixin.pay.order.model;

import cn.jzyunqi.common.third.weixin.common.model.WeixinRspV1;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wiiyaya
 * @since 2021/7/20.
 */
@Getter
@Setter
public class UnifiedOrderRsp extends WeixinRspV1 {

    /**
     * 【预支付交易会话标识】 预支付交易会话标识。用于后续接口调用中使用，该值有效期为2小时
     */
    private String prepayId;

    /**
     * 【二维码链接】 此URL用于生成支付二维码，然后提供给用户扫码支付。
     * 注意：code_url并非固定值，使用时按照URL格式转成二维码即可。
     * code_url有效期为2小时，过期后扫码不能再发起支付。
     */
    private String codeUrl;

    /**
     * 【支付跳转链接】 h5_url为拉起微信支付收银台的中间页面，可通过访问该URL来拉起微信客户端，完成支付，h5_url的有效期为5分钟。
     */
    private String h5Url;
}
