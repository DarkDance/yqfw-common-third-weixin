package cn.jzyunqi.common.third.weixin.miniapp.qrcode;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.third.weixin.miniapp.qrcode.model.QrcodeParam;
import jakarta.annotation.Resource;

/**
 * @author wiiyaya
 * @since 2025/9/12
 */
public class WxMaQrcodeApi {

    @Resource
    private WxMaQrcodeApiProxy wxMaQrcodeApiProxy;

    //小程序码与小程序链接 - 获取不限制的小程序码
    public org.springframework.core.io.Resource getClientToken(String access_token, QrcodeParam request) throws BusinessException {
        return wxMaQrcodeApiProxy.getCodeUnLimit(access_token, request);
    }
}
