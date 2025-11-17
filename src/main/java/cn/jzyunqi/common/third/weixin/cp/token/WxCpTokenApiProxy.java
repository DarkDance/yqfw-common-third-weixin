package cn.jzyunqi.common.third.weixin.cp.token;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.third.weixin.common.WxHttpExchange;
import cn.jzyunqi.common.third.weixin.cp.token.model.IpListData;
import cn.jzyunqi.common.third.weixin.mp.token.model.ClientTokenData;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

/**
 * @author wiiyaya
 * @since 2025/11/17
 */
@WxHttpExchange
@HttpExchange(url = "https://aqyapi.weixin.qq.com", accept = {"application/json"})
public interface WxCpTokenApiProxy {

    //获取 Access token
    @GetExchange(url = "/cgi-bin/gettoken")
    ClientTokenData getCpToken(@RequestParam String corpid, @RequestParam String corpsecret) throws BusinessException;

    //获取域名对应的ip
    @GetExchange(url = "/cgi-bin/get_api_domain_ip")
    IpListData getDomainIpList(@RequestParam String access_token) throws BusinessException;

    //获取回调IP地址
    @GetExchange(url = "/cgi-bin/getcallbackip")
    IpListData getCallbackIpList(@RequestParam String access_token) throws BusinessException;

}
