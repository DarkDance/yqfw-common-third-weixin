package cn.jzyunqi.common.third.weixin.mp.client;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.feature.redis.Cache;
import cn.jzyunqi.common.feature.redis.RedisHelper;
import cn.jzyunqi.common.third.weixin.mp.model.enums.InfoScope;
import cn.jzyunqi.common.third.weixin.mp.model.request.item.LineColorData;
import cn.jzyunqi.common.third.weixin.mp.callback.model.MsgDetailCb;
import cn.jzyunqi.common.third.weixin.mp.callback.model.MsgSimpleCb;
import cn.jzyunqi.common.third.weixin.mp.token.model.JsApiTicketRedisDto;
import cn.jzyunqi.common.third.weixin.mp.model.request.QrcodeParam;
import cn.jzyunqi.common.third.weixin.mp.callback.model.ReplyMsgData;
import cn.jzyunqi.common.third.weixin.mp.token.model.JsApiTicketRsp;
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
     * 获得jsapi_ticket
     */
    private static final String WX_JS_API_TICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=%s&type=jsapi";

    /**
     * 以公众号的名义获取用户信息
     */
    private static final String WX_USER_INFO_URL = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=%s&openid=%s&lang=zh_CN";

    /**
     * 临时素材处理接口
     */
    private static final String WX_TMP_MTL_ART_ADD = "https://api.weixin.qq.com/cgi-bin/media/uploadnews?access_token=%s"; //上传原创图文消息


    /**
     * 原创推文
     */
    private static final String WX_MASS_SEND = "https://api.weixin.qq.com/cgi-bin/message/mass/sendall?access_token=%s"; //根据标签进行群发
    //private static final String WX_MASS_OPEN_ID_SEND = "https://api.weixin.qq.com/cgi-bin/message/mass/send?access_token=ACCESS_TOKEN"; //根据OpenID列表群发（仅服务号）
    //private static final String WX_MASS_DEL = "https://api.weixin.qq.com/cgi-bin/message/mass/delete?access_token=ACCESS_TOKEN"; //删除群发
    private static final String WX_MASS_PREVIEW = "https://api.weixin.qq.com/cgi-bin/message/mass/preview?access_token=%s"; //预览接口
    //private static final String WX_MASS_STATUS_GET = "https://api.weixin.qq.com/cgi-bin/message/mass/get?access_token=ACCESS_TOKEN"; //查询群发消息发送状态
    //private static final String WX_MASS_SPEED_GET = "https://api.weixin.qq.com/cgi-bin/message/mass/speed/get?access_token=ACCESS_TOKEN"; //查询群发速度
    //private static final String WX_MASS_SPEED_EDIT = "https://api.weixin.qq.com/cgi-bin/message/mass/speed/set?access_token=ACCESS_TOKEN"; //设置群发速度

    /**
     * 获取小程序二维码
     */
    private static final String WX_MP_QRCODE = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=%s";

    /**
     * 需要签名的字符串
     */
    private static final String WX_JS_API_TICKET_SIGN = "jsapi_ticket=%s&noncestr=%s&timestamp=%s&url=%s";

    /**
     * 消息加密块填充长度，1 - 255，微信为32
     */
    private static final int BLOCK_SIZE = 32;

    private static final String RESPONSE_MSG =
            "<xml>\n"
                    + "<Encrypt><![CDATA[%1$s]]></Encrypt>\n"
                    + "<MsgSignature><![CDATA[%2$s]]></MsgSignature>\n"
                    + "<TimeStamp>%3$s</TimeStamp>\n"
                    + "<Nonce><![CDATA[%4$s]]></Nonce>\n"
                    + "</xml>";

    private static final String REPLAY_MESSAGE_FAILED = "failed";
    private static final String REPLAY_MESSAGE_SUCCESS = "success";

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

    @FunctionalInterface
    public interface MessageReplyCallback {

        /**
         * 根据消息内容回复相应的信息
         *
         * @param decryptNotice 解密后的消息
         * @return null -> 代表成功 / 其它代表返回数据
         */
        Object prepareReplyMessage(MsgDetailCb decryptNotice) throws BusinessException;
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
     * 获得jsapi_ticket权限签名
     *
     * @param url 需要签名的url
     * @return 签名结果
     */
    public Map<String, Object> getJsApiTicket(String url) throws BusinessException {

        JsApiTicketRedisDto ticketResult = (JsApiTicketRedisDto) redisHelper.vGet(tokenCache, jsApiTicketKey);
        if (ticketResult != null && LocalDateTime.now().isBefore(ticketResult.getExpireTime())) {
            return this.ticketSign(ticketResult.getTicket(), url);
        }

        JsApiTicketRsp jsApiTicketRsp;
        try {
            URI accessTokenUri = new URIBuilder(String.format(WX_JS_API_TICKET_URL, this.getInterfaceToken())).build();

            RequestEntity<Map<String, String>> requestEntity = new RequestEntity<>(HttpMethod.GET, accessTokenUri);
            ResponseEntity<JsApiTicketRsp> weixinUserRsp = restTemplate.exchange(requestEntity, JsApiTicketRsp.class);
            jsApiTicketRsp = weixinUserRsp.getBody();
        } catch (Exception e) {
            log.error("======WeixinCgiHelper getJsApiTicket other error:", e);
            throw new BusinessException("common_error_wx_get_jsapi_ticket_error");
        }
        //微信不管成功还是失败，返回的都是200，需要通过额外的字段来判断是否真的成功
        if (jsApiTicketRsp != null && "0".equals(jsApiTicketRsp.getErrorCode())) {
            //把token放入缓存
            ticketResult = new JsApiTicketRedisDto();
            ticketResult.setTicket(jsApiTicketRsp.getTicket()); //授权ticket
            ticketResult.setExpireTime(LocalDateTime.now().plusSeconds(jsApiTicketRsp.getExpiresIn()).minusSeconds(120)); //过期时间
            redisHelper.vPut(tokenCache, jsApiTicketKey, ticketResult);

            return this.ticketSign(jsApiTicketRsp.getTicket(), url);
        } else {
            if (jsApiTicketRsp == null) {
                jsApiTicketRsp = new JsApiTicketRsp();
            }
            log.error("======WeixinCgiHelper getJsApiTicket 200 error[{}][{}]", jsApiTicketRsp.getErrorCode(), jsApiTicketRsp.getErrorMsg());
            throw new BusinessException("common_error_wx_get_jsapi_ticket_failed");
        }
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
     * 加密消息
     *
     * @param msg 消息字符串
     * @return 加密结果
     */
    private String encryptMsg(String msg) {
        Byte[] randomStrBytes = ArrayUtils.toObject(RandomUtilPlus.String.randomAlphanumeric(16).getBytes(StringUtilPlus.UTF_8));
        Byte[] textBytes = ArrayUtils.toObject(msg.getBytes(StringUtilPlus.UTF_8));
        Byte[] networkBytesOrder = ArrayUtils.toObject(getNetworkBytesOrder(textBytes.length));
        Byte[] appidBytes = ArrayUtils.toObject(appId.getBytes(StringUtilPlus.UTF_8));

        // randomStr + networkBytesOrder + text + appid
        List<Byte> byteList = new ArrayList<>();
        byteList.addAll(CollectionUtilPlus.Array.asList(randomStrBytes));
        byteList.addAll(CollectionUtilPlus.Array.asList(networkBytesOrder));
        byteList.addAll(CollectionUtilPlus.Array.asList(textBytes));
        byteList.addAll(CollectionUtilPlus.Array.asList(appidBytes));

        // ... + pad: 使用自定义的填充方式对明文进行补位填充
        Byte[] padBytes = ArrayUtils.toObject(this.pkcs7Encode(byteList.size()));
        byteList.addAll(CollectionUtilPlus.Array.asList(padBytes));

        // 获得最终的字节流, 未加密
        byte[] unencrypted = ArrayUtils.toPrimitive(byteList.toArray(new Byte[0]));

        try {
            // 加密
            String encryptMsg = DigestUtilPlus.AES.encryptCBCNoPadding(unencrypted, msgEncodingAesKey, msgEncodingAesIv, true);
            //消息签名
            Long timestamp = System.currentTimeMillis() / 1000;
            String nonceStr = RandomUtilPlus.String.randomAlphanumeric(32);
            String signature = signString(msgToken, timestamp.toString(), nonceStr, encryptMsg);

            return String.format(RESPONSE_MSG, encryptMsg, signature, timestamp, nonceStr);
        } catch (Exception e) {
            log.error("======encryptMsg error:", e);
            return null;
        }
    }

    /**
     * 解密并回复通知消息
     *
     * @param msgSimpleCb url参数
     * @param msgDetailCb notice对象
     * @param callback    回调
     * @return 回复，根据情况返回回调数据或"failed"或"success"
     */
    public Object replyMessageNotice(MsgSimpleCb msgSimpleCb, MsgDetailCb msgDetailCb, MessageReplyCallback callback) {
        //验证安全签名，如果有消息体，校验后解码消息体
        if (msgDetailCb == null) {//没有消息体，属于连接测试
            String needSign = signString(msgToken, msgSimpleCb.getTimestamp(), msgSimpleCb.getNonce()).equals(msgSimpleCb.getSignature()) ? msgSimpleCb.getEchostr() : REPLAY_MESSAGE_FAILED;
            log.debug("======WeixinCgiHelper replyMessageNotice needSign:{}, echostr:{}", needSign, msgSimpleCb.getEchostr());
            return needSign;
        } else {
            if (StringUtilPlus.isNotEmpty(msgDetailCb.getEncrypt())) {//消息是密文传输，需要解密
                String selfMsgSignature = signString(msgToken, msgSimpleCb.getTimestamp(), msgSimpleCb.getNonce(), msgDetailCb.getEncrypt());//组装消息验证码
                if (!selfMsgSignature.equals(msgSimpleCb.getMsg_signature())) {
                    return REPLAY_MESSAGE_FAILED;
                }
                //解密消息
                msgDetailCb = decryptMsg(msgDetailCb.getEncrypt());
                if (msgDetailCb == null) {
                    return REPLAY_MESSAGE_FAILED;
                }
            }

            try {
                Object reply = callback.prepareReplyMessage(msgDetailCb);
                log.debug("======WeixinCgiHelper replyMessageNotice reply:{}", reply);
                return reply == null ? REPLAY_MESSAGE_SUCCESS : reply;
            } catch (BusinessException e) {
                log.error("======WeixinCgiHelper replyMessageNotice error:", e);
                return REPLAY_MESSAGE_FAILED;
            }
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

    /**
     * 解密消息
     *
     * @param encryptMsg 加密字符串
     * @return 解密结果
     */
    private MsgDetailCb decryptMsg(String encryptMsg) {
        try {
            String rst = DigestUtilPlus.AES.decryptCBCNoPadding(DigestUtilPlus.Base64.decodeBase64(encryptMsg), msgEncodingAesKey, msgEncodingAesIv);
            // 去除补位字符
            byte[] bytes = pkcs7Decode(rst.getBytes(StringUtilPlus.UTF_8));
            // 分离16位随机字符串,网络字节序和AppId
            int xmlLength = recoverNetworkBytesOrder(Arrays.copyOfRange(bytes, 16, 20));
            //检查id是否正确
            String fromAppId = new String(Arrays.copyOfRange(bytes, 20 + xmlLength, bytes.length), StringUtilPlus.UTF_8);
            if (fromAppId.equals(appId)) {
                //装换消息对象
                String decryptMsg = new String(Arrays.copyOfRange(bytes, 20, 20 + xmlLength), StringUtilPlus.UTF_8);
                JAXBContext context = JAXBContext.newInstance(MsgDetailCb.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();
                return (MsgDetailCb) unmarshaller.unmarshal(IOUtilPlus.toInputStream(decryptMsg, StringUtilPlus.UTF_8));
            } else {
                log.error("======appId not match!");
                return null;
            }
        } catch (Exception e) {
            log.error("======decryptMsg error:", e);
            return null;
        }
    }

    /**
     * 生成4个字节的网络字节序
     */
    private byte[] getNetworkBytesOrder(int sourceNumber) {
        byte[] orderBytes = new byte[4];
        orderBytes[3] = (byte) (sourceNumber & 0xFF);
        orderBytes[2] = (byte) (sourceNumber >> 8 & 0xFF);
        orderBytes[1] = (byte) (sourceNumber >> 16 & 0xFF);
        orderBytes[0] = (byte) (sourceNumber >> 24 & 0xFF);
        return orderBytes;
    }

    /**
     * 还原4个字节的网络字节序
     */
    private int recoverNetworkBytesOrder(byte[] orderBytes) {
        int sourceNumber = 0;
        for (int i = 0; i < 4; i++) {
            sourceNumber <<= 8;
            sourceNumber |= orderBytes[i] & 0xff;
        }
        return sourceNumber;
    }

    /**
     * 删除解密后明文的补位字符
     *
     * @param decrypted 解密后的明文
     * @return 删除补位字符后的明文
     */
    private byte[] pkcs7Decode(byte[] decrypted) {
        int pad = (int) decrypted[decrypted.length - 1];
        if (pad < 1 || pad > BLOCK_SIZE) {
            pad = 0;
        }
        return Arrays.copyOfRange(decrypted, 0, decrypted.length - pad);
    }

    /**
     * 获得对明文进行补位填充的字节.
     *
     * @param count 需要进行填充补位操作的明文字节个数
     * @return 补齐用的字节数组
     */
    private byte[] pkcs7Encode(int count) {
        // 计算需要填充的位数
        int amountToPad = BLOCK_SIZE - (count % BLOCK_SIZE);
        // 获得补位所用的字符
        char padChr = (char) (byte) (amountToPad & 0xFF);
        return String.valueOf(padChr).repeat(amountToPad).getBytes(StringUtilPlus.UTF_8);
    }

    /**
     * 字符串签名
     *
     * @param needSignArray 待签名字符串
     * @return 签名
     */
    private String signString(String... needSignArray) {
        String needSign = Stream.of(needSignArray)
                .filter(StringUtilPlus::isNotEmpty)
                .sorted(String::compareTo)
                .collect(Collectors.joining(StringUtilPlus.EMPTY));
        return DigestUtilPlus.SHA.sign(needSign, DigestUtilPlus.SHAAlgo._1, Boolean.FALSE);
    }

    /**
     * 签名url
     *
     * @param ticket 授权ticket
     * @param url    需要签名的url
     * @return 签名结果
     */
    private Map<String, Object> ticketSign(String ticket, String url) {

        long timestamp = System.currentTimeMillis() / 1000;//从1970年1月1日00:00:00至今的秒数
        String nonceStr = RandomUtilPlus.String.randomAlphanumeric(32);
        //注意这里参数名必须全部小写，且必须有序
        String needSign = String.format(WX_JS_API_TICKET_SIGN, ticket, nonceStr, timestamp, url);
        String signature = DigestUtilPlus.SHA.sign(needSign, DigestUtilPlus.SHAAlgo._1, Boolean.FALSE);

        Map<String, Object> params = new HashMap<>();
        params.put("appId", appId);
        params.put("timestamp", timestamp);
        params.put("nonceStr", nonceStr);
        params.put("signature", signature);
        return params;
    }
}
