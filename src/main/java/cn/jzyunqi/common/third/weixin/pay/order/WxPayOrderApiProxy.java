package cn.jzyunqi.common.third.weixin.pay.order;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.third.weixin.common.WxHttpExchange;
import cn.jzyunqi.common.third.weixin.pay.order.model.RefundOrderParam;
import cn.jzyunqi.common.third.weixin.pay.order.model.OrderData;
import cn.jzyunqi.common.third.weixin.pay.order.model.OrderRefundData;
import cn.jzyunqi.common.third.weixin.pay.order.model.UnifiedOrderParam;
import cn.jzyunqi.common.third.weixin.pay.order.model.UnifiedOrderRsp;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/**
 * @author wiiyaya
 * @since 2024/9/25
 */
@WxHttpExchange
@HttpExchange(url = "https://api.mch.weixin.qq.com", accept = {"application/json"}, contentType = "application/json")
public interface WxPayOrderApiProxy {

    //JSAPI、小程序支付 - 预下单
    @PostExchange(url = "/v3/pay/transactions/jsapi")
    UnifiedOrderRsp unifiedJsapiOrder(@RequestBody UnifiedOrderParam request) throws BusinessException;

    //小程序支付 - 订单查询(微信支付订单号)
    @PostExchange(url = "/v3/pay/transactions/id/{transaction_id}")
    OrderData queryOrderByTransactionId(@PathVariable String transaction_id, @RequestParam String mchid) throws BusinessException;

    //小程序支付 - 订单查询(商户订单号)
    @PostExchange(url = "/v3/pay/transactions/out-trade-no/{out_trade_no}")
    OrderData queryOrderByOutTradeNo(@PathVariable String out_trade_no, @RequestParam String mchid) throws BusinessException;

    //小程序支付 - 退款申请
    @PostExchange(url = "/v3/refund/domestic/refunds")
    OrderRefundData refundApply(@RequestBody RefundOrderParam request) throws BusinessException;

    //Native支付 - 预下单
    @PostExchange(url = "/v3/pay/transactions/native")
    UnifiedOrderRsp unifiedNativeOrder(@RequestBody UnifiedOrderParam request) throws BusinessException;

    //H5支付 - 预下单
    @PostExchange(url = "/v3/pay/transactions/h5")
    UnifiedOrderRsp unifiedH5Order(@RequestBody UnifiedOrderParam request) throws BusinessException;

    //APP支付 - 预下单
    @PostExchange(url = "/v3/pay/transactions/app")
    UnifiedOrderRsp unifiedAppOrder(@RequestBody UnifiedOrderParam request) throws BusinessException;

}
