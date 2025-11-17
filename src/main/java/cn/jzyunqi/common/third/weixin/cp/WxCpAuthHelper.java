package cn.jzyunqi.common.third.weixin.cp;

import cn.jzyunqi.common.third.weixin.mp.WxMpAuth;

/**
 * @author wiiyaya
 * @since 2025/11/17
 */
public interface WxCpAuthHelper {

    WxCpAuth chooseWxCpAuth(String corpId);
}
