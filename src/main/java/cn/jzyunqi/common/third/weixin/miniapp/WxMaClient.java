package cn.jzyunqi.common.third.weixin.miniapp;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.third.weixin.miniapp.qrcode.WxMaQrcodeApiProxy;
import cn.jzyunqi.common.third.weixin.miniapp.qrcode.model.QrcodeParam;
import cn.jzyunqi.common.third.weixin.mp.WxMpClient;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

/**
 * @author wiiyaya
 * @since 2024/9/25
 */
@Slf4j
public class WxMaClient {

    @Resource
    private WxMaClientConfig wxMaClientConfig;

    @Resource
    private WxMaQrcodeApiProxy wxMaQrcodeApiProxy;

    public final Qrcode qrcode = new Qrcode();

    public class Qrcode {

        //小程序码与小程序链接 - 获取不限制的小程序码
        public org.springframework.core.io.Resource getClientToken(String access_token, QrcodeParam request) throws BusinessException {
            return wxMaQrcodeApiProxy.getClientToken(access_token, request);
        }

    }
}
