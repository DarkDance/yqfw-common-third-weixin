package cn.jzyunqi.common.third.weixin.mp;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.feature.redis.LockType;
import cn.jzyunqi.common.feature.redis.RedisHelper;
import cn.jzyunqi.common.third.weixin.common.constant.WxCache;
import cn.jzyunqi.common.third.weixin.common.model.WeixinRsp;
import cn.jzyunqi.common.third.weixin.mp.callback.model.MsgDetailCb;
import cn.jzyunqi.common.third.weixin.mp.callback.model.MsgSimpleCb;
import cn.jzyunqi.common.third.weixin.mp.callback.model.ReplyMsgData;
import cn.jzyunqi.common.third.weixin.mp.kefu.WxMpKfApiProxy;
import cn.jzyunqi.common.third.weixin.mp.kefu.enums.TypingType;
import cn.jzyunqi.common.third.weixin.mp.kefu.model.WxMpKfAccountParam;
import cn.jzyunqi.common.third.weixin.mp.kefu.model.WxMpKfListRsp;
import cn.jzyunqi.common.third.weixin.mp.kefu.model.WxMpKfMsgListParam;
import cn.jzyunqi.common.third.weixin.mp.kefu.model.WxMpKfMsgListRsp;
import cn.jzyunqi.common.third.weixin.mp.kefu.model.WxMpKfSessionData;
import cn.jzyunqi.common.third.weixin.mp.kefu.model.WxMpKfSessionListRsp;
import cn.jzyunqi.common.third.weixin.mp.kefu.model.WxMpKfTypingParam;
import cn.jzyunqi.common.third.weixin.mp.mass.WxMpMassApiProxy;
import cn.jzyunqi.common.third.weixin.mp.mass.model.MassRsp;
import cn.jzyunqi.common.third.weixin.mp.material.WxMpMaterialApiProxy;
import cn.jzyunqi.common.third.weixin.mp.material.enums.MaterialType;
import cn.jzyunqi.common.third.weixin.mp.material.model.WxMpMaterialCountData;
import cn.jzyunqi.common.third.weixin.mp.material.model.WxMpMaterialNewsData;
import cn.jzyunqi.common.third.weixin.mp.material.model.WxMpMaterialSearchParam;
import cn.jzyunqi.common.third.weixin.mp.material.model.WxMpMaterialSearchRsp;
import cn.jzyunqi.common.third.weixin.mp.material.model.WxMpMaterialVideoRsp;
import cn.jzyunqi.common.third.weixin.mp.material.model.WxMpMediaData;
import cn.jzyunqi.common.third.weixin.mp.menu.WxMpMenuApiProxy;
import cn.jzyunqi.common.third.weixin.mp.menu.model.WxMenuData;
import cn.jzyunqi.common.third.weixin.mp.menu.model.WxMenuRsp;
import cn.jzyunqi.common.third.weixin.mp.menu.model.WxMenuTryMatchParam;
import cn.jzyunqi.common.third.weixin.mp.menu.model.WxMpSelfMenuInfoRsp;
import cn.jzyunqi.common.third.weixin.common.enums.InfoScope;
import cn.jzyunqi.common.third.weixin.mp.token.WxMpTokenApiProxy;
import cn.jzyunqi.common.third.weixin.mp.token.enums.TicketType;
import cn.jzyunqi.common.third.weixin.mp.token.model.ClientTokenData;
import cn.jzyunqi.common.third.weixin.mp.token.model.ClientTokenRedisDto;
import cn.jzyunqi.common.third.weixin.mp.token.model.TicketRedisDto;
import cn.jzyunqi.common.third.weixin.mp.token.model.TicketRsp;
import cn.jzyunqi.common.third.weixin.mp.token.model.WxJsapiSignature;
import cn.jzyunqi.common.third.weixin.mp.user.WxMpUserApiProxy;
import cn.jzyunqi.common.third.weixin.mp.user.model.MpUserData;
import cn.jzyunqi.common.utils.CollectionUtilPlus;
import cn.jzyunqi.common.utils.DateTimeUtilPlus;
import cn.jzyunqi.common.utils.DigestUtilPlus;
import cn.jzyunqi.common.utils.IOUtilPlus;
import cn.jzyunqi.common.utils.RandomUtilPlus;
import cn.jzyunqi.common.utils.StringUtilPlus;
import jakarta.annotation.Resource;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 公众号客户端
 *
 * @author wiiyaya
 * @since 2024/9/23
 */
@Slf4j
public class WxMpClient {

    private static final String WX_PUBLIC_BASE_FMT_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=%s&state=#wechat_redirect";
    private static final String WX_JS_API_TICKET_SIGN = "jsapi_ticket=%s&noncestr=%s&timestamp=%s&url=%s";

    @Resource
    private WxMpTokenApiProxy wxMpTokenApiProxy;

    @Resource
    private WxMpKfApiProxy wxMpKfApiProxy;

    @Resource
    private WxMpMenuApiProxy wxMpMenuApiProxy;

    @Resource
    private WxMpMaterialApiProxy wxMpMaterialApiProxy;

    @Resource
    private WxMpUserApiProxy wxMpUserApiProxy;

    @Resource
    private WxMpMassApiProxy wxMpMassApiProxy;

    @Resource
    private WxMpClientConfig wxMpClientConfig;

    @Resource
    private RedisHelper redisHelper;

    public final Kefu kf = new Kefu();
    public final Menu menu = new Menu();
    public final Material material = new Material();
    public final Callback cb = new Callback();
    public final User user = new User();
    public final Mass mass = new Mass();

    public class Kefu {
        //客服管理 - 添加客服账号（添加后不可用，需要再邀请）
        public WeixinRsp kfAccountAdd(WxMpKfAccountParam request) throws BusinessException {
            return wxMpKfApiProxy.kfAccountAdd(getClientToken(), request);
        }

        //客服管理 - 邀请绑定客服账号
        public WeixinRsp kfAccountInviteWorker(WxMpKfAccountParam request) throws BusinessException {
            return wxMpKfApiProxy.kfAccountInviteWorker(getClientToken(), request);
        }

        //客服管理 - 修改客服账号
        public WeixinRsp kfAccountUpdate(WxMpKfAccountParam request) throws BusinessException {
            return wxMpKfApiProxy.kfAccountUpdate(getClientToken(), request);
        }

        //客服管理 - 删除客服账号
        public WeixinRsp kfAccountDel(String kfAccount) throws BusinessException {
            WxMpKfAccountParam request = new WxMpKfAccountParam();
            request.setKfAccount(kfAccount);
            return wxMpKfApiProxy.kfAccountDel(getClientToken(), request);
        }

        //客服管理 - 设置客服账号的头像，文件大小为5M以内
        public WeixinRsp kfAccountUploadHeadImg(String kfAccount, MultipartFile media) throws BusinessException {
            return wxMpKfApiProxy.kfAccountUploadHeadImg(getClientToken(), kfAccount, media);
        }

        //客服管理 - 获取所有客服账号
        public WxMpKfListRsp kfList() throws BusinessException {
            return wxMpKfApiProxy.kfList(getClientToken());
        }

        //客服管理 - 获取所有客服在线账号
        public WxMpKfListRsp kfOnlineList() throws BusinessException {
            return wxMpKfApiProxy.kfOnlineList(getClientToken());
        }

        //客服消息 - 发消息
        public String sendKefuMessageWithResponse(ReplyMsgData request) throws BusinessException {
            return wxMpKfApiProxy.sendKefuMessageWithResponse(getClientToken(), request);
        }

        //客服消息 - 发送客服输入状态
        public String sendKfTypingState(String openid, TypingType command) throws BusinessException {
            WxMpKfTypingParam request = new WxMpKfTypingParam();
            request.setToUser(openid);
            request.setCommand(command);
            return wxMpKfApiProxy.sendKfTypingState(getClientToken(), request);
        }

        //会话控制 - 创建会话
        public String kfSessionCreate(String openid, String kfAccount) throws BusinessException {
            WxMpKfSessionData request = new WxMpKfSessionData();
            request.setOpenid(openid);
            request.setKfAccount(kfAccount);
            return wxMpKfApiProxy.kfSessionCreate(getClientToken(), request);
        }

        //会话控制 - 关闭会话
        public String kfSessionClose(String openid, String kfAccount) throws BusinessException {
            WxMpKfSessionData request = new WxMpKfSessionData();
            request.setOpenid(openid);
            request.setKfAccount(kfAccount);
            return wxMpKfApiProxy.kfSessionClose(getClientToken(), request);
        }

        //会话控制 - 获取客户会话状态
        public WxMpKfSessionData kfSessionInfo(String openid) throws BusinessException {
            return wxMpKfApiProxy.kfSessionInfo(getClientToken(), openid);
        }

        //会话控制 - 获取客服会话列表
        public WxMpKfSessionListRsp kfSessionList(String kfAccount) throws BusinessException {
            return wxMpKfApiProxy.kfSessionList(getClientToken(), kfAccount);
        }

        //会话控制 - 获取未接入会话列表
        public WxMpKfSessionListRsp kfSessionWaitCase() throws BusinessException {
            return wxMpKfApiProxy.kfSessionWaitCase(getClientToken());
        }

        //获取聊天记录
        public WxMpKfMsgListRsp kfMsgList(LocalDateTime startTime, LocalDateTime endTime, Long msgId, Integer number) throws BusinessException {
            WxMpKfMsgListParam request = new WxMpKfMsgListParam();
            request.setStartTime(DateTimeUtilPlus.toEpochSecond(startTime));
            request.setEndTime(DateTimeUtilPlus.toEpochSecond(endTime));
            request.setMsgId(msgId);
            request.setNumber(number);
            return wxMpKfApiProxy.kfMsgList(getClientToken(), request);
        }
    }

    public class Menu {
        //自定义菜单 - 创建接口
        public WeixinRsp menuCreate(WxMenuData request) throws BusinessException {
            if (request.getMatchRule() != null) {
                return wxMpMenuApiProxy.selfMenuCreate(getClientToken(), request);
            } else {
                return wxMpMenuApiProxy.menuCreate(getClientToken(), request);
            }
        }

        //自定义菜单 - 查询接口（包括官网设置的菜单和AIP设置的菜单）
        public WxMpSelfMenuInfoRsp allMenuInfo() throws BusinessException {
            return wxMpMenuApiProxy.allMenuInfo(getClientToken());
        }

        //自定义菜单 - 删除接口
        public WeixinRsp menuDelete() throws BusinessException {
            return wxMpMenuApiProxy.menuDelete(getClientToken());
        }

        //自定义菜单 - 删除个性化菜单
        public WeixinRsp selfMenuDelete(String menuId) throws BusinessException {
            WxMenuData request = new WxMenuData();
            request.setMenuId(menuId);
            return wxMpMenuApiProxy.selfMenuDelete(getClientToken(), request);
        }

        //自定义菜单 - 测试个性化菜单匹配结果
        public WxMenuData selfMenuTryMatch(String openId) throws BusinessException {
            WxMenuTryMatchParam request = new WxMenuTryMatchParam();
            request.setUserId(openId);
            return wxMpMenuApiProxy.selfMenuTryMatch(getClientToken(), request);
        }

        //自定义菜单 - 获取自定义菜单配置(只能查询API定义的菜单)
        public WxMenuRsp selfMenuGet() throws BusinessException {
            return wxMpMenuApiProxy.selfMenuGet(getClientToken());
        }
    }

    public class Material {
        //素材管理 - 新增临时素材
        public WxMpMediaData mediaUpload(MaterialType type, MultipartFile media) throws BusinessException {
            return wxMpMaterialApiProxy.mediaUpload(getClientToken(), type, media);
        }

        //素材管理 - 获取临时素材
        public File mediaDownload(String mediaId) throws BusinessException {
            return wxMpMaterialApiProxy.mediaDownload(getClientToken(), mediaId);
        }

        //素材管理 - 获取高清语音素材
        public File jssdkMediaDownload(String mediaId) throws BusinessException {
            return wxMpMaterialApiProxy.jssdkMediaDownload(getClientToken(), mediaId);
        }

        //素材管理 - 新增图片永久素材
        public WxMpMediaData mediaImgUpload(MultipartFile media) throws BusinessException {
            return wxMpMaterialApiProxy.mediaImgUpload(getClientToken(), media);
        }

        //素材管理 - 新增其它永久素材
        public WxMpMediaData materialOtherUpload(MaterialType type, MultipartFile media) throws BusinessException {
            return wxMpMaterialApiProxy.materialOtherUpload(getClientToken(), type, media);
        }

        //素材管理 - 图文永久素材获取
        public WxMpMaterialNewsData materialNewsInfo(String mediaId) throws BusinessException {
            WxMpMediaData request = new WxMpMediaData();
            request.setMediaId(mediaId);
            return wxMpMaterialApiProxy.materialNewsInfo(getClientToken(), request);
        }

        //素材管理 - 视频永久素材获取
        public WxMpMaterialVideoRsp materialVideoInfo(String mediaId) throws BusinessException {
            WxMpMediaData request = new WxMpMediaData();
            request.setMediaId(mediaId);
            return wxMpMaterialApiProxy.materialVideoInfo(getClientToken(), request);
        }

        //素材管理 - 其它永久素材获取
        public InputStream materialOtherDownload(String mediaId) throws BusinessException {
            WxMpMediaData request = new WxMpMediaData();
            request.setMediaId(mediaId);
            return wxMpMaterialApiProxy.materialOtherDownload(getClientToken(), request);
        }

        //素材管理 - 删除永久素材
        public WeixinRsp materialDelete(String mediaId) throws BusinessException {
            WxMpMediaData request = new WxMpMediaData();
            request.setMediaId(mediaId);
            return wxMpMaterialApiProxy.materialDelete(getClientToken(), request);
        }

        //素材管理 - 获取永久素材总数
        public WxMpMaterialCountData materialCount() throws BusinessException {
            return wxMpMaterialApiProxy.materialCount(getClientToken());
        }

        //素材管理 - 获取永久素材列表
        public WxMpMaterialSearchRsp materialBatchGet(MaterialType type, Integer offset, Integer count) throws BusinessException {
            WxMpMaterialSearchParam request = new WxMpMaterialSearchParam();
            request.setType(type);
            request.setOffset(offset);
            request.setCount(count);
            return wxMpMaterialApiProxy.materialBatchGet(getClientToken(), request);
        }
    }

    public class Callback {
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
         * 消息加密块填充长度，1 - 255，微信为32
         */
        private static final int BLOCK_SIZE = 32;

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
                String needSign = signString(wxMpClientConfig.getMsgToken(), msgSimpleCb.getTimestamp(), msgSimpleCb.getNonce()).equals(msgSimpleCb.getSignature()) ? msgSimpleCb.getEchostr() : REPLAY_MESSAGE_FAILED;
                log.debug("======WeixinCgiHelper replyMessageNotice needSign:{}, echostr:{}", needSign, msgSimpleCb.getEchostr());
                return needSign;
            } else {
                if (StringUtilPlus.isNotEmpty(msgDetailCb.getEncrypt())) {//消息是密文传输，需要解密
                    String selfMsgSignature = signString(wxMpClientConfig.getMsgToken(), msgSimpleCb.getTimestamp(), msgSimpleCb.getNonce(), msgDetailCb.getEncrypt());//组装消息验证码
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
         * 加密消息
         *
         * @param msg 消息字符串
         * @return 加密结果
         */
        private String encryptMsg(String msg) {
            Byte[] randomStrBytes = ArrayUtils.toObject(RandomUtilPlus.String.randomAlphanumeric(16).getBytes(StringUtilPlus.UTF_8));
            Byte[] textBytes = ArrayUtils.toObject(msg.getBytes(StringUtilPlus.UTF_8));
            Byte[] networkBytesOrder = ArrayUtils.toObject(getNetworkBytesOrder(textBytes.length));
            Byte[] appidBytes = ArrayUtils.toObject(wxMpClientConfig.getAppId().getBytes(StringUtilPlus.UTF_8));

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
                String encryptMsg = DigestUtilPlus.AES.encryptCBCNoPadding(unencrypted, wxMpClientConfig.getMsgEncodingAesKey(), wxMpClientConfig.getMsgEncodingAesIv(), true);
                //消息签名
                Long timestamp = System.currentTimeMillis() / 1000;
                String nonceStr = RandomUtilPlus.String.randomAlphanumeric(32);
                String signature = signString(wxMpClientConfig.getMsgToken(), timestamp.toString(), nonceStr, encryptMsg);

                return String.format(RESPONSE_MSG, encryptMsg, signature, timestamp, nonceStr);
            } catch (Exception e) {
                log.error("======encryptMsg error:", e);
                return null;
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
                String rst = DigestUtilPlus.AES.decryptCBCNoPadding(DigestUtilPlus.Base64.decodeBase64(encryptMsg), wxMpClientConfig.getMsgEncodingAesKey(), wxMpClientConfig.getMsgEncodingAesIv());
                // 去除补位字符
                byte[] bytes = pkcs7Decode(rst.getBytes(StringUtilPlus.UTF_8));
                // 分离16位随机字符串,网络字节序和AppId
                int xmlLength = recoverNetworkBytesOrder(Arrays.copyOfRange(bytes, 16, 20));
                //检查id是否正确
                String fromAppId = new String(Arrays.copyOfRange(bytes, 20 + xmlLength, bytes.length), StringUtilPlus.UTF_8);
                if (fromAppId.equals(wxMpClientConfig.getAppId())) {
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
    }

    public class User {
        //用户管理 - 获取用户基本信息
        public MpUserData userInfo(String openid) throws BusinessException {
            return userInfo(openid, null);
        }

        public MpUserData userInfo(String openid, String lang) throws BusinessException {
            lang = StringUtilPlus.defaultString(lang, "zh_CN");
            return wxMpUserApiProxy.userInfo(getClientToken(), openid, lang);
        }
    }

    public class Mass {
        //基础消息能力 - 群发接口 - 根据标签进行群发
        MassRsp massGroupMessageSend(ReplyMsgData request, boolean preview) throws BusinessException {
            if (preview) {
                return wxMpMassApiProxy.massMessagePreview(getClientToken(), request);
            } else {
                return wxMpMassApiProxy.massGroupMessageSend(getClientToken(), request);
            }
        }
    }

    public WxJsapiSignature createJsapiSignature(String url) throws BusinessException {
        long timestamp = System.currentTimeMillis() / 1000;//从1970年1月1日00:00:00至今的秒数
        String nonceStr = RandomUtilPlus.String.randomAlphanumeric(32);
        String jsapiTicket = getTicket(TicketType.JSAPI);
        //注意这里参数名必须全部小写，且必须有序
        String needSign = String.format(WX_JS_API_TICKET_SIGN, jsapiTicket, nonceStr, timestamp, url);
        String signature = DigestUtilPlus.SHA.sign(needSign, DigestUtilPlus.SHAAlgo._1, Boolean.FALSE);

        WxJsapiSignature jsapiSignature = new WxJsapiSignature();
        jsapiSignature.setAppId(wxMpClientConfig.getAppId());
        jsapiSignature.setTimestamp(timestamp);
        jsapiSignature.setNonceStr(nonceStr);
        jsapiSignature.setUrl(url);
        jsapiSignature.setSignature(signature);
        return jsapiSignature;
    }

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

        String redirectUri = wxMpClientConfig.getUserSyncUrl() + DigestUtilPlus.Base64.encodeBase64String(realPage.toString().getBytes());
        return String.format(WX_PUBLIC_BASE_FMT_URL, wxMpClientConfig.getAppId(), URLEncoder.encode(redirectUri, StringUtilPlus.UTF_8), infoScope);
    }

    private String getClientToken() throws BusinessException {
        ClientTokenRedisDto clientToken = (ClientTokenRedisDto) redisHelper.vGet(WxCache.WX_MP, wxMpClientConfig.getClientTokenKey());
        if (clientToken != null && LocalDateTime.now().isBefore(clientToken.getExpireTime())) {
            return clientToken.getToken();
        }
        Lock lock = redisHelper.getLock(WxCache.WX_MP, wxMpClientConfig.getClientTokenKey().concat(":lock"), LockType.NORMAL);
        long timeOutMillis = System.currentTimeMillis() + 3000;
        boolean locked = false;
        try {
            do {
                // 防止多线程同时获取accessToken
                clientToken = (ClientTokenRedisDto) redisHelper.vGet(WxCache.WX_MP, wxMpClientConfig.getClientTokenKey());
                if (clientToken != null && LocalDateTime.now().isBefore(clientToken.getExpireTime())) {
                    return clientToken.getToken();
                }

                locked = lock.tryLock(100, TimeUnit.MILLISECONDS);
                if (!locked && System.currentTimeMillis() > timeOutMillis) {
                    throw new InterruptedException("获取accessToken超时：获取时间超时");
                }
            } while (!locked);

            //获取到锁的服务可以去获取accessToken
            ClientTokenData clientTokenData = wxMpTokenApiProxy.getClientToken(wxMpClientConfig.getAppId(), wxMpClientConfig.getAppSecret());
            clientToken = new ClientTokenRedisDto();
            clientToken.setToken(clientTokenData.getAccessToken()); //获取到的凭证
            clientToken.setExpireTime(LocalDateTime.now().plusSeconds(clientTokenData.getExpiresIn()).minusSeconds(120)); //凭证有效时间，单位：秒

            redisHelper.vPut(WxCache.WX_MP, wxMpClientConfig.getClientTokenKey(), clientToken);
            return clientTokenData.getAccessToken();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if (locked) {
                lock.unlock();
            }
        }
    }

    private String getTicket(TicketType type) throws BusinessException {
        String ticketKey = chooseTicketKey(type);
        TicketRedisDto ticketRedisDto = (TicketRedisDto) redisHelper.vGet(WxCache.WX_MP, ticketKey);
        if (ticketRedisDto != null && LocalDateTime.now().isBefore(ticketRedisDto.getExpireTime())) {
            return ticketRedisDto.getTicket();
        }
        Lock lock = redisHelper.getLock(WxCache.WX_MP, ticketKey.concat(":lock"), LockType.NORMAL);
        long timeOutMillis = System.currentTimeMillis() + 3000;
        boolean locked = false;
        try {
            do {
                // 防止多线程同时获取accessToken
                ticketRedisDto = (TicketRedisDto) redisHelper.vGet(WxCache.WX_MP, ticketKey);
                if (ticketRedisDto != null && LocalDateTime.now().isBefore(ticketRedisDto.getExpireTime())) {
                    return ticketRedisDto.getTicket();
                }

                locked = lock.tryLock(100, TimeUnit.MILLISECONDS);
                if (!locked && System.currentTimeMillis() > timeOutMillis) {
                    throw new InterruptedException("获取ticket超时：获取时间超时:" + type);
                }
            } while (!locked);

            //获取到锁的服务可以去获取accessToken
            TicketRsp ticketRsp = wxMpTokenApiProxy.getTicket(getClientToken(), type.getCode());
            ticketRedisDto = new TicketRedisDto();
            ticketRedisDto.setTicket(ticketRsp.getTicket()); //获取到的凭证
            ticketRedisDto.setExpireTime(LocalDateTime.now().plusSeconds(ticketRsp.getExpiresIn()).minusSeconds(120)); //凭证有效时间，单位：秒

            redisHelper.vPut(WxCache.WX_MP, ticketKey, ticketRedisDto);
            return ticketRsp.getTicket();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if (locked) {
                lock.unlock();
            }
        }
    }

    private String chooseTicketKey(TicketType type) {
        return switch (type) {
            case JSAPI -> wxMpClientConfig.getJsapiTicketKey();
            case WX_CARD -> wxMpClientConfig.getWxCardTicketKey();
            case SDK -> wxMpClientConfig.getSdkTicketKey();
        };
    }
}
