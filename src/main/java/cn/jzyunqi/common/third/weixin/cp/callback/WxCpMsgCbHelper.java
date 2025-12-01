package cn.jzyunqi.common.third.weixin.cp.callback;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.third.weixin.mp.callback.model.MsgDetailCb;
import cn.jzyunqi.common.third.weixin.mp.callback.model.MsgSimpleCb;
import cn.jzyunqi.common.third.weixin.mp.callback.model.ReplyMsgData;
import cn.jzyunqi.common.utils.CollectionUtilPlus;
import cn.jzyunqi.common.utils.DigestUtilPlus;
import cn.jzyunqi.common.utils.IOUtilPlus;
import cn.jzyunqi.common.utils.RandomUtilPlus;
import cn.jzyunqi.common.utils.StringUtilPlus;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author wiiyaya
 * @since 2025/9/12
 */
@Slf4j
public class WxCpMsgCbHelper {

    private static final String RESPONSE_MSG =
            """
                    <xml>
                    <Encrypt><![CDATA[%1$s]]></Encrypt>
                    <MsgSignature><![CDATA[%2$s]]></MsgSignature>
                    <TimeStamp>%3$s</TimeStamp>
                    <Nonce><![CDATA[%4$s]]></Nonce>
                    </xml>""";

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
        ReplyMsgData apply(MsgDetailCb decryptNotice) throws BusinessException;
    }

    /**
     * 解密并回复通知消息
     *
     * @param msgSimpleCb url参数
     * @param msgDetailCb notice对象
     * @param callback    回调
     * @return 回复，根据情况返回回调数据或"failed"或"success"
     */
    public static Object replyMessageNotice(String appId, String verificationToken, String encryptKey, MsgSimpleCb msgSimpleCb, MsgDetailCb msgDetailCb, MessageReplyCallback callback) {
        //验证安全签名，如果有消息体，校验后解码消息体
        if (msgDetailCb == null) {//没有消息体，属于连接测试
            String selfMsgSignature = signString(verificationToken, msgSimpleCb.getTimestamp(), msgSimpleCb.getNonce(), msgSimpleCb.getEchostr());
            if (!selfMsgSignature.equals(msgSimpleCb.getMsg_signature())) {
                return REPLAY_MESSAGE_FAILED;
            }
            //解密消息
            String echostr = decryptMsg(appId, encryptKey, msgSimpleCb.getEchostr());
            if (StringUtilPlus.isBlank(echostr)) {
                return REPLAY_MESSAGE_FAILED;
            }
            log.debug("======WxCpClient replyMessageNotice needSign:{}, echostr:{}", selfMsgSignature, echostr);
            return echostr;
        } else {
            if (StringUtilPlus.isNotEmpty(msgDetailCb.getEncrypt())) {//消息是密文传输，需要解密
                String selfMsgSignature = signString(verificationToken, msgSimpleCb.getTimestamp(), msgSimpleCb.getNonce(), msgDetailCb.getEncrypt());//组装消息验证码
                if (!selfMsgSignature.equals(msgSimpleCb.getMsg_signature())) {
                    return REPLAY_MESSAGE_FAILED;
                }
                //解密消息
                String decryptMsg = decryptMsg(appId, encryptKey, msgDetailCb.getEncrypt());
                msgDetailCb = parseMsgDetail(decryptMsg);
                if (msgDetailCb == null) {
                    return REPLAY_MESSAGE_FAILED;
                }
            }

            try {
                Object reply = callback.apply(msgDetailCb);
                log.debug("======WxMpClient replyMessageNotice reply:{}", reply);
                return reply == null ? REPLAY_MESSAGE_SUCCESS : reply;
            } catch (BusinessException e) {
                log.error("======WxMpClient replyMessageNotice error:", e);
                return REPLAY_MESSAGE_FAILED;
            }
        }
    }

    /**
     * 将解码的字符串消息转换成对象
     *
     * @param decryptMsg 解码后的消息明文字符串
     * @return 消息对象
     */
    private static MsgDetailCb parseMsgDetail(String decryptMsg) {
        try {
            JAXBContext context = JAXBContext.newInstance(MsgDetailCb.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (MsgDetailCb) unmarshaller.unmarshal(IOUtilPlus.toInputStream(decryptMsg, StringUtilPlus.UTF_8));
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 加密消息
     *
     * @param msg 消息字符串
     * @return 加密结果
     */
    private static String encryptMsg(String appId, String verificationToken, String encryptKey, String msg) {
        Byte[] randomStrBytes = ArrayUtils.toObject(RandomUtilPlus.String.nextAlphanumeric(16).getBytes(StringUtilPlus.UTF_8));
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
        Byte[] padBytes = ArrayUtils.toObject(pkcs7Encode(byteList.size()));
        byteList.addAll(CollectionUtilPlus.Array.asList(padBytes));

        // 获得最终的字节流, 未加密
        byte[] unencrypted = ArrayUtils.toPrimitive(byteList.toArray(new Byte[0]));

        try {
            // 加密
            byte[] aesKey = DigestUtilPlus.Base64.decodeBase64(encryptKey);
            byte[] aesIv = Arrays.copyOfRange(Base64.decodeBase64(aesKey), 0, 16);
            String encryptMsg = DigestUtilPlus.AES.encryptCBCNoPadding(unencrypted, aesKey, aesIv, true);
            //消息签名
            Long timestamp = System.currentTimeMillis() / 1000;
            String nonceStr = RandomUtilPlus.String.nextAlphanumeric(32);
            String signature = signString(verificationToken, timestamp.toString(), nonceStr, encryptMsg);

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
    private static String decryptMsg(String appId, String encryptKey, String encryptMsg) {
        try {
            byte[] aesKey = DigestUtilPlus.Base64.decodeBase64(encryptKey + "=");
            byte[] aesIv = Arrays.copyOfRange(aesKey, 0, 16);
            String rst = DigestUtilPlus.AES.decryptCBCNoPadding(DigestUtilPlus.Base64.decodeBase64(encryptMsg), aesKey, aesIv);
            // 去除补位字符
            byte[] bytes = pkcs7Decode(rst.getBytes(StringUtilPlus.UTF_8));
            // 分离16位随机字符串,网络字节序和AppId
            int xmlLength = recoverNetworkBytesOrder(Arrays.copyOfRange(bytes, 16, 20));
            //检查id是否正确
            String fromAppId = new String(Arrays.copyOfRange(bytes, 20 + xmlLength, bytes.length), StringUtilPlus.UTF_8);
            if (fromAppId.equals(appId)) {
                //消息字符串
                return new String(Arrays.copyOfRange(bytes, 20, 20 + xmlLength), StringUtilPlus.UTF_8);
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
    private static byte[] pkcs7Decode(byte[] decrypted) {
        int pad = (int) decrypted[decrypted.length - 1];
        if (pad < 1 || pad > BLOCK_SIZE) {
            pad = 0;
        }
        return Arrays.copyOfRange(decrypted, 0, decrypted.length - pad);
    }

    /**
     * 还原4个字节的网络字节序
     */
    private static int recoverNetworkBytesOrder(byte[] orderBytes) {
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
    private static String signString(String... needSignArray) {
        String needSign = Stream.of(needSignArray)
                .filter(StringUtilPlus::isNotEmpty)
                .sorted(String::compareTo)
                .collect(Collectors.joining(StringUtilPlus.EMPTY));
        return DigestUtilPlus.SHA.sign(needSign, DigestUtilPlus.SHAAlgo._1, Boolean.FALSE);
    }

    /**
     * 生成4个字节的网络字节序
     */
    private static byte[] getNetworkBytesOrder(int sourceNumber) {
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
    private static byte[] pkcs7Encode(int count) {
        // 计算需要填充的位数
        int amountToPad = BLOCK_SIZE - (count % BLOCK_SIZE);
        // 获得补位所用的字符
        char padChr = (char) (byte) (amountToPad & 0xFF);
        return String.valueOf(padChr).repeat(amountToPad).getBytes(StringUtilPlus.UTF_8);
    }
}
