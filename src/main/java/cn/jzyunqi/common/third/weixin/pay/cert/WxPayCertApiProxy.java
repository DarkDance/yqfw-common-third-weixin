package cn.jzyunqi.common.third.weixin.pay.cert;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.third.weixin.common.WxHttpExchange;
import cn.jzyunqi.common.third.weixin.common.model.WeixinRspV3;
import cn.jzyunqi.common.third.weixin.pay.cert.model.PlantCertData;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.List;

/**
 * @author wiiyaya
 * @since 2024/9/25
 */
@WxHttpExchange
@HttpExchange(url = "https://api.mch.weixin.qq.com", accept = {"application/json"}, contentType = "application/json")
public interface WxPayCertApiProxy {

    //安全工具 - 平台证书下载
    @PostExchange(url = "/v3/certificates")
    WeixinRspV3<List<PlantCertData>> certDownload() throws BusinessException;
}
