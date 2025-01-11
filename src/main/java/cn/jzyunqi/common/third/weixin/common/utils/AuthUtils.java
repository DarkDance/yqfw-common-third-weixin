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

    private static final String IN_WX_USER_CODE_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=%s&state=%s#wechat_redirect";
    private static final String OUT_WX_USER_CODE_URL = "https://open.weixin.qq.com/connect/qrconnect?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_login&state=%s#wechat_redirect";

    /**
     * 获取微信公众号授权链接
     *
     * @param appId           微信各种号id
     * @param redirectRootUrl 重定向URL
     * @param infoScope       授权信息范围
     * @return 微信公众号授权链接
     */
    public static String inWxUserAuthCodeUrl(String appId, String redirectRootUrl, InfoScope infoScope) {
        return userAuthUrl(true, appId, redirectRootUrl, infoScope, null, null, null);
    }

    public static String inWxUserAuthCodeUrl(String appId, String redirectRootUrl, InfoScope infoScope, String nextPage, String nextPageHash, Map<String, String> nextPageParams) {
        return userAuthUrl(true, appId, redirectRootUrl, infoScope, nextPage, nextPageHash, nextPageParams);
    }

    public static String outWxUserAuthUrl(String appId, String redirectRootUrl) {
        return userAuthUrl(false, appId, redirectRootUrl, null, null, null, null);
    }

    public static String outWxUserAuthUrl(String appId, String redirectRootUrl, String nextPage, String nextPageHash, Map<String, String> nextPageParams) {
        return userAuthUrl(false, appId, redirectRootUrl, null, nextPage, nextPageHash, nextPageParams);
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
        String nonceStr = RandomUtilPlus.String.nextAlphanumeric(32);
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

    /**
     * 获取微信公众号授权链接
     *
     * @param inWeixin        是否在微信内部授权
     * @param appId           微信各种号id
     * @param redirectRootUrl 重定向URL
     * @param infoScope       授权信息范围
     * @param nextPage        应用内页面
     * @param nextPageHash    应用内页面锚点hash
     * @param nextPageParams  重定向参数
     * @return 微信公众号授权链接
     */
    private static String userAuthUrl(boolean inWeixin, String appId, String redirectRootUrl, InfoScope infoScope, String nextPage, String nextPageHash, Map<String, String> nextPageParams) {
        String nextPageLink = "";
        if (StringUtilPlus.isNotBlank(nextPage)) {
            StringBuilder realPage = new StringBuilder();
            realPage.append(nextPage);
            realPage.append("?_=");
            realPage.append(System.currentTimeMillis());
            if (CollectionUtilPlus.Map.isNotEmpty(nextPageParams)) {
                realPage.append("&");
                realPage.append(CollectionUtilPlus.Map.getUrlParam(nextPageParams, false, false, true));
            }
            if (StringUtilPlus.isNotBlank(nextPageHash)) {
                realPage.append("#/");
                realPage.append(nextPageHash);
            }
            nextPageLink = DigestUtilPlus.Base64.encodeBase64String(realPage.toString().getBytes());
        }
        String redirectUri = redirectRootUrl + nextPageLink;
        String status = RandomUtilPlus.String.nextAlphanumeric(32);
        if (inWeixin) {
            return String.format(IN_WX_USER_CODE_URL, appId, URLEncoder.encode(redirectUri, StringUtilPlus.UTF_8), infoScope, status);
        } else {
            return String.format(OUT_WX_USER_CODE_URL, appId, URLEncoder.encode(redirectUri, StringUtilPlus.UTF_8), status);
        }
    }
}
