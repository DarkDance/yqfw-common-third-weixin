package cn.jzyunqi.common.third.weixin.mp;

import cn.jzyunqi.common.utils.DigestUtilPlus;
import lombok.Getter;
import org.apache.commons.codec.binary.Base64;

import java.util.Arrays;

/**
 * @author wiiyaya
 * @since 2024/9/23
 */
public interface WxMpClientConfig {

    String getAppId();

    String getAppSecret();

    String getMsgToken();

    String getUserSyncUrl();

    byte[] getMsgEncodingAesKey();

    byte[] getMsgEncodingAesIv();

    String getJsapiTicketKey();

    String getWxCardTicketKey();

    String getSdkTicketKey();

    String getClientTokenKey();
}
