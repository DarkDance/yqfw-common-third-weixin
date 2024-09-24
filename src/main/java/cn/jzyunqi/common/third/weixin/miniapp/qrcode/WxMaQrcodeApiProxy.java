package cn.jzyunqi.common.third.weixin.miniapp.qrcode;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.third.weixin.common.WxHttpExchange;
import cn.jzyunqi.common.third.weixin.mp.model.request.QrcodeParam;
import cn.jzyunqi.common.third.weixin.mp.token.model.ClientTokenData;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

/**
 * @author wiiyaya
 * @since 2024/9/24
 */
@WxHttpExchange
@HttpExchange(url = "https://api.weixin.qq.com", accept = {"application/json"})
public interface WxMaQrcodeApiProxy {
    //小程序码与小程序链接 - 获取不限制的小程序码
    @GetExchange(url = "/wxa/getwxacodeunlimit")
    Byte[] getClientToken(@RequestParam String access_token, @RequestBody QrcodeParam request) throws BusinessException;

}
