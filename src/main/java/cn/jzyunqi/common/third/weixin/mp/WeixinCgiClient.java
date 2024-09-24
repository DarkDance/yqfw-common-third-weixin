package cn.jzyunqi.common.third.weixin.mp;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.feature.redis.Cache;
import cn.jzyunqi.common.feature.redis.RedisHelper;
import cn.jzyunqi.common.third.weixin.mp.model.enums.InfoScope;
import cn.jzyunqi.common.third.weixin.mp.model.request.item.LineColorData;
import cn.jzyunqi.common.third.weixin.mp.callback.model.MsgDetailCb;
import cn.jzyunqi.common.third.weixin.mp.callback.model.MsgSimpleCb;
import cn.jzyunqi.common.third.weixin.mp.token.model.TicketRedisDto;
import cn.jzyunqi.common.third.weixin.mp.model.request.QrcodeParam;
import cn.jzyunqi.common.third.weixin.mp.callback.model.ReplyMsgData;
import cn.jzyunqi.common.third.weixin.mp.token.model.TicketRsp;
import cn.jzyunqi.common.third.weixin.mp.model.response.MassRsp;
import cn.jzyunqi.common.third.weixin.mp.model.response.MpUserInfoRsp;
import cn.jzyunqi.common.utils.BooleanUtilPlus;
import cn.jzyunqi.common.utils.CollectionUtilPlus;
import cn.jzyunqi.common.utils.DigestUtilPlus;
import cn.jzyunqi.common.utils.IOUtilPlus;
import cn.jzyunqi.common.utils.RandomUtilPlus;
import cn.jzyunqi.common.utils.StringUtilPlus;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.hc.core5.net.URIBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 大部分为公众号接口
 *
 * @author wiiyaya
 * @since 2018/5/29.
 */
@Slf4j
@Deprecated
public class WeixinCgiClient {

    /**
     * 获取用户授权
     */
    private static final String WX_PUBLIC_BASE_FMT_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=%s&state=#wechat_redirect";

    /**
     * 以公众号的名义获取用户信息
     */
    private static final String WX_USER_INFO_URL = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=%s&openid=%s&lang=zh_CN";


    /**
     * 原创推文
     */
    private static final String WX_MASS_SEND = "https://api.weixin.qq.com/cgi-bin/message/mass/sendall?access_token=%s"; //根据标签进行群发
    private static final String WX_MASS_PREVIEW = "https://api.weixin.qq.com/cgi-bin/message/mass/preview?access_token=%s"; //预览接口

    /**
     * 获取小程序二维码
     */
    private static final String WX_MP_QRCODE = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=%s";

    /**
     * 需要签名的字符串
     */
    private static final String WX_JS_API_TICKET_SIGN = "jsapi_ticket=%s&noncestr=%s&timestamp=%s&url=%s";







    /**
     * 公众号/小程序唯一凭证
     */
    private final String appId;

    /**
     * 公众号/小程序唯一凭证密钥
     */
    private final String appSecret;

    /**
     * 关联小程序唯一凭证
     */
    //private String wpAppId;

    /**
     * 消息签名token(对称)
     */
    private String msgToken;

    /**
     * 消息体加密密钥
     */
    private byte[] msgEncodingAesKey;

    /**
     * 消息体加密向量
     */
    private byte[] msgEncodingAesIv;

    /**
     * 用户信息同步中转页面
     */
    private String userSyncUrl;

    /**
     * 接口token key
     */
    private final String interfaceTokenKey;

    /**
     * js ticket key
     */
    private final String jsApiTicketKey;

    private final Cache tokenCache;

    private final RestTemplate restTemplate;

    private final RedisHelper redisHelper;

    public WeixinCgiClient(Cache tokenCache, String appId, String appSecret, RestTemplate restTemplate, RedisHelper redisHelper) {
        this.appId = appId;
        this.appSecret = appSecret;
        this.restTemplate = restTemplate;
        this.redisHelper = redisHelper;
        this.tokenCache = tokenCache;
        this.interfaceTokenKey = "INTERFACE_TOKEN:" + appId;
        this.jsApiTicketKey = "JS_API_TICKET:" + appId;
    }

    public WeixinCgiClient(Cache tokenCache, String appId, String appSecret, String msgToken, String msgEncodingAesKey, String userSyncUrl, RestTemplate restTemplate, RedisHelper redisHelper) {
        this(tokenCache, appId, appSecret, restTemplate, redisHelper);
        this.msgToken = msgToken;
        this.msgEncodingAesKey = DigestUtilPlus.Base64.decodeBase64(msgEncodingAesKey);
        this.msgEncodingAesIv = Arrays.copyOfRange(Base64.decodeBase64(this.msgEncodingAesKey), 0, 16);
        this.userSyncUrl = userSyncUrl;
    }



    /**
     * 组装同步网址
     *
     * @param redirectPage   转发页面
     * @param redirectParams 页面参数
     * @return 网址
     */
    public String prepareUserSyncUrl(String redirectPage, String hash, Map<String, String> redirectParams, InfoScope infoScope) throws BusinessException {
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

        String redirectUri = userSyncUrl + DigestUtilPlus.Base64.encodeBase64String(realPage.toString().getBytes());
        return String.format(WX_PUBLIC_BASE_FMT_URL, appId, URLEncoder.encode(redirectUri, StringUtilPlus.UTF_8), infoScope);
    }

    /**
     * 获取access_token
     *
     * @return 获取access_token
     */
    public String getInterfaceToken() throws BusinessException {
        return null;
    }

    /**
     * 根据openId 获取用户信息
     *
     * @param openId openId
     * @return 用户信息
     */
    public MpUserInfoRsp getMpUserInfo(String openId) throws BusinessException {
        MpUserInfoRsp userInfoRsp;
        try {
            URI weixinUserInfoUri = new URIBuilder(String.format(WX_USER_INFO_URL, this.getInterfaceToken(), openId)).build();

            RequestEntity<Map<String, String>> requestEntity = new RequestEntity<>(HttpMethod.GET, weixinUserInfoUri);
            ResponseEntity<MpUserInfoRsp> weixinUserRsp = restTemplate.exchange(requestEntity, MpUserInfoRsp.class);
            userInfoRsp = weixinUserRsp.getBody();
        } catch (Exception e) {
            log.error("======WeixinCgiHelper getPublicUserInfo other error:", e);
            throw new BusinessException("common_error_wx_get_public_user_info_error");
        }
        //微信不管成功还是失败，返回的都是200，需要通过额外的字段来判断是否真的成功
        if (userInfoRsp != null && StringUtilPlus.isEmpty(userInfoRsp.getErrorCode())) {
            return userInfoRsp;
        } else {
            if (userInfoRsp == null) {
                userInfoRsp = new MpUserInfoRsp();
            }
            log.error("======WeixinCgiHelper getPublicUserInfo 200 error[{}][{}]", userInfoRsp.getErrorCode(), userInfoRsp.getErrorMsg());
            throw new BusinessException("common_error_wx_get_public_user_info_failed");
        }
    }

    /**
     * 根据标签进行群发
     */
    public MassRsp sendArticles(ReplyMsgData replyMsgParam, Boolean preview) throws BusinessException {
        MassRsp massRsp;
        try {
            URI sendArticlesUri;
            if (BooleanUtilPlus.isTrue(preview)) {
                sendArticlesUri = new URIBuilder(String.format(WX_MASS_PREVIEW, this.getInterfaceToken())).build();
            } else {
                sendArticlesUri = new URIBuilder(String.format(WX_MASS_SEND, this.getInterfaceToken())).build();
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            RequestEntity<ReplyMsgData> requestEntity = new RequestEntity<>(replyMsgParam, headers, HttpMethod.POST, sendArticlesUri);
            ResponseEntity<MassRsp> responseEntity = restTemplate.exchange(requestEntity, MassRsp.class);
            massRsp = responseEntity.getBody();
        } catch (Exception e) {
            log.error("======WeixinCgiHelper sendArticles other error:", e);
            throw new BusinessException("common_error_wx_ppt_mtl_add_error");
        }

        if (massRsp != null && "0".equals(massRsp.getErrorCode())) {
            return massRsp;
        } else {
            if (massRsp == null) {
                massRsp = new MassRsp();
            }
            log.error("======WeixinCgiHelper sendArticles 200 error [{}][{}]:", massRsp.getErrorCode(), massRsp.getErrorMsg());
            throw new BusinessException("common_error_wx_ppt_mtl_add_failed");
        }
    }





    /**
     * 根据openId 获取用户信息
     *
     * @param scene 场景字段的值会作为 query 参数传递给小程序
     * @return 用户信息
     */
    public byte[] getMpSceneCode(String page, String scene) throws BusinessException {
        byte[] rst;
        MediaType returnedType;
        try {
            URI weixinUserInfoUri = new URIBuilder(String.format(WX_MP_QRCODE, this.getInterfaceToken())).build();

            QrcodeParam qrcodeParam = new QrcodeParam();
            qrcodeParam.setScene(scene);
            qrcodeParam.setPage(page);
            qrcodeParam.setWidth(430);
            qrcodeParam.setAutoColor(Boolean.FALSE);
            qrcodeParam.setLineColor(new LineColorData(0, 0, 0));
            qrcodeParam.setHyAline(Boolean.FALSE);

            RequestEntity<QrcodeParam> requestEntity = new RequestEntity<>(qrcodeParam, HttpMethod.POST, weixinUserInfoUri);
            ResponseEntity<byte[]> responseEntity = restTemplate.exchange(requestEntity, byte[].class);
            rst = Optional.ofNullable(responseEntity.getBody()).orElse(new byte[]{});
            returnedType = responseEntity.getHeaders().getContentType();
        } catch (Exception e) {
            log.error("======WeixinCgiHelper getMpSceneCode other error:", e);
            throw new BusinessException("common_error_wx_get_mp_scene_code_error");
        }

        if (returnedType != null && returnedType.includes(MediaType.IMAGE_JPEG)) {
            return rst;
        } else {
            log.error("======WeixinCgiHelper getMpSceneCode 200 error[{}]", new String(rst, StringUtilPlus.UTF_8));
            throw new BusinessException("common_error_wx_get_mp_scene_code_failed");
        }
    }





}
