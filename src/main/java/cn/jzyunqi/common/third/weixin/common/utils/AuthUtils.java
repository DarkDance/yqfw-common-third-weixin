package cn.jzyunqi.common.third.weixin.common.utils;

import cn.jzyunqi.common.third.weixin.common.enums.InfoScope;
import cn.jzyunqi.common.utils.CollectionUtilPlus;
import cn.jzyunqi.common.utils.DigestUtilPlus;
import cn.jzyunqi.common.utils.RandomUtilPlus;
import cn.jzyunqi.common.utils.StringUtilPlus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;

import java.net.URLEncoder;
import java.util.Map;

/**
 * @author wiiyaya
 * @since 2024/9/26
 */
@Slf4j
public class AuthUtils {

    private static final String WX_PUBLIC_BASE_FMT_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=%s&state=#wechat_redirect";


    /**
     * 获取微信公众号授权链接
     *
     * @param appId          公众号id
     * @param redirectDomain 重定向域名
     * @param redirectPage   重定向页面
     * @param hash           页面hash
     * @param redirectParams 重定向参数
     * @param infoScope      授权信息范围
     * @return 微信公众号授权链接
     */
    public String prepareUserSyncUrl(String appId, String redirectDomain, String redirectPage, String hash, Map<String, String> redirectParams, InfoScope infoScope) {
        StringBuilder realPage = new StringBuilder();
        realPage.append(redirectPage);
        realPage.append("?_=");
        realPage.append(System.currentTimeMillis());
        if (CollectionUtilPlus.Map.isNotEmpty(redirectParams)) {
            realPage.append("&");
            realPage.append(CollectionUtilPlus.Map.getUrlParam(redirectParams, false, false, true));
        }
        if (StringUtilPlus.isNotBlank(hash)) {
            realPage.append("#/");
            realPage.append(hash);
        }

        String redirectUri = redirectDomain + DigestUtilPlus.Base64.encodeBase64String(realPage.toString().getBytes());
        return String.format(WX_PUBLIC_BASE_FMT_URL, appId, URLEncoder.encode(redirectUri, StringUtilPlus.UTF_8), infoScope);
    }

    /**
     * 获取微信支付授权凭证
     *
     * @param merchantId           商户号
     * @param merchantSerialNumber 商户证书序列号
     * @param merchantPrivateKey   商户私钥
     * @param method               请求方法
     * @param path                 请求路径
     * @param body                 请求体
     * @return 微信支付授权凭证
     */
    public static String genAuthToken(String merchantId, String merchantSerialNumber, String merchantPrivateKey, HttpMethod method, String path, String body) {
        Long timestamp = System.currentTimeMillis() / 1000;
        String nonceStr = RandomUtilPlus.String.randomAlphanumeric(32);
        String needSignContent = StringUtilPlus.join(
                method, StringUtilPlus.ENTER,
                path, StringUtilPlus.ENTER,
                timestamp, StringUtilPlus.ENTER,
                nonceStr, StringUtilPlus.ENTER,
                StringUtilPlus.defaultIfBlank(body, StringUtilPlus.EMPTY), StringUtilPlus.ENTER
        );
        log.debug("=====needSignContent: [{}]", needSignContent);
        String sign = StringUtilPlus.EMPTY;
        try {
            sign = DigestUtilPlus.RSA256.signPrivateKey(needSignContent.getBytes(StringUtilPlus.UTF_8), DigestUtilPlus.Base64.decodeBase64(merchantPrivateKey), Boolean.TRUE);
        } catch (Exception e) {
            log.error("=====headerSign error: ", e);
        }
        return String.format(
                "WECHATPAY2-SHA256-RSA2048 mchid=\"%s\",serial_no=\"%s\",nonce_str=\"%s\",timestamp=\"%s\",signature=\"%s\"",
                merchantId,
                merchantSerialNumber,
                nonceStr,
                timestamp,
                sign
        );
    }
}
