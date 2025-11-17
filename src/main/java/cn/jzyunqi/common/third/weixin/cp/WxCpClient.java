package cn.jzyunqi.common.third.weixin.cp;

import cn.jzyunqi.common.third.weixin.mp.token.WxMpTokenApi;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wiiyaya
 * @since 2025/11/17
 */
@Slf4j
public class WxCpClient {

    @Resource
    public WxMpTokenApi token;
}
