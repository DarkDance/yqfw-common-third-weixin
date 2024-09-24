package cn.jzyunqi.common.third.weixin.mp;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.feature.redis.Cache;
import cn.jzyunqi.common.feature.redis.RedisHelper;
import cn.jzyunqi.common.third.weixin.mp.model.enums.InfoScope;
import cn.jzyunqi.common.third.weixin.mp.model.request.item.LineColorData;
import cn.jzyunqi.common.third.weixin.mp.model.request.QrcodeParam;
import cn.jzyunqi.common.third.weixin.mp.callback.model.ReplyMsgData;
import cn.jzyunqi.common.third.weixin.mp.model.response.MassRsp;
import cn.jzyunqi.common.third.weixin.mp.user.model.MpUserData;
import cn.jzyunqi.common.utils.BooleanUtilPlus;
import cn.jzyunqi.common.utils.CollectionUtilPlus;
import cn.jzyunqi.common.utils.DigestUtilPlus;
import cn.jzyunqi.common.utils.StringUtilPlus;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.hc.core5.net.URIBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

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
     * 原创推文
     */
    private static final String WX_MASS_SEND = "https://api.weixin.qq.com/cgi-bin/message/mass/sendall?access_token=%s"; //根据标签进行群发
    private static final String WX_MASS_PREVIEW = "https://api.weixin.qq.com/cgi-bin/message/mass/preview?access_token=%s"; //预览接口

    /**
     * 获取小程序二维码
     */
    private static final String WX_MP_QRCODE = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=%s";


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
     * 获取access_token
     *
     * @return 获取access_token
     */
    public String getInterfaceToken() throws BusinessException {
        return null;
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
