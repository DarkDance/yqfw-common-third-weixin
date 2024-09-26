package cn.jzyunqi.common.third.weixin.pay.order;

import cn.jzyunqi.common.third.weixin.pay.WxPayClientConfig;
import cn.jzyunqi.common.utils.DigestUtilPlus;
import cn.jzyunqi.common.utils.RandomUtilPlus;
import cn.jzyunqi.common.utils.StringUtilPlus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.ClientRequest;

/**
 * @author wiiyaya
 * @since 2024/9/26
 */
@Slf4j
public class AuthUtils {

    public static String genAuthToken(WxPayClientConfig wxPayClientConfig, ClientRequest request) {
        Long timestamp = System.currentTimeMillis() / 1000;
        String nonceStr = RandomUtilPlus.String.randomAlphanumeric(32);
        String needSignContent = StringUtilPlus.join(
                request.method(), StringUtilPlus.ENTER,
                request.url().getPath(), StringUtilPlus.ENTER,
                timestamp, StringUtilPlus.ENTER,
                nonceStr, StringUtilPlus.ENTER,
                StringUtilPlus.defaultIfBlank(request.body().toString(), StringUtilPlus.EMPTY), StringUtilPlus.ENTER
        );
        String sign = StringUtilPlus.EMPTY;
        try {
            sign = DigestUtilPlus.RSA256.signPrivateKey(needSignContent.getBytes(StringUtilPlus.UTF_8), DigestUtilPlus.Base64.decodeBase64(wxPayClientConfig.getMerchantPrivateKey()), Boolean.TRUE);
        } catch (Exception e) {
            log.error("=====headerSign error: ", e);
        }
        return String.format(
                "WECHATPAY2-SHA256-RSA2048 mchid=\"%s\",serial_no=\"%s\",nonce_str=\"%s\",timestamp=\"%s\",signature=\"%s\"",
                wxPayClientConfig.getMerchantId(),
                wxPayClientConfig.getMerchantSerialNumber(),
                nonceStr,
                timestamp,
                sign
        );
    }
}
