package cn.jzyunqi.common.third.weixin.mp.token;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.third.weixin.mp.WxMpClient;
import cn.jzyunqi.common.third.weixin.mp.token.model.WxJsapiSignature;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author wiiyaya
 * @since 2024/10/17
 */
@Slf4j
public abstract class AWxMpJssdkController {

    @Resource
    protected WxMpClient wxMpClient;

    /**
     * 获取微信小程序jssdk ticket
     *
     * @param url encode后的页面全名 | 例如：let pageFullName = encodeURIComponent(location.origin + location.pathname + location.search);
     */
    @RequestMapping
    public WxJsapiSignature jssdkTicket(@RequestParam String url) throws BusinessException {
        return wxMpClient.createJsapiSignature(url);
    }
}
