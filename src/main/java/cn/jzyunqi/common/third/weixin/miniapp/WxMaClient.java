package cn.jzyunqi.common.third.weixin.miniapp;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.third.weixin.miniapp.qrcode.WxMaQrcodeApi;
import cn.jzyunqi.common.third.weixin.miniapp.qrcode.WxMaQrcodeApiProxy;
import cn.jzyunqi.common.third.weixin.miniapp.qrcode.model.QrcodeParam;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wiiyaya
 * @since 2024/9/25
 */
@Slf4j
public class WxMaClient {

    @Resource
    public WxMaQrcodeApi qrcode;
}
