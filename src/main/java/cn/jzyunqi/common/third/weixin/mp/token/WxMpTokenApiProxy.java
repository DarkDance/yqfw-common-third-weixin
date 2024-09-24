package cn.jzyunqi.common.third.weixin.mp.token;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.third.weixin.common.WxHttpExchange;
import cn.jzyunqi.common.third.weixin.mp.token.model.ClientTokenData;
import cn.jzyunqi.common.third.weixin.mp.token.model.TicketRsp;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

/**
 * 开始开发API
 *
 * @author wiiyaya
 * @since 2024/9/23
 */
@WxHttpExchange
@HttpExchange(url = "https://api.weixin.qq.com", accept = {"application/json"})
public interface WxMpTokenApiProxy {

    //获取 Access token
    @GetExchange(url = "/cgi-bin/token?grant_type=client_credential")
    ClientTokenData getClientToken(@RequestParam String appid, @RequestParam String secret) throws BusinessException;

    //获取 Ticket
    @GetExchange(url = "/cgi-bin/ticket/getticket")
    TicketRsp getTicket(@RequestParam String access_token, @RequestParam String type) throws BusinessException;
}
