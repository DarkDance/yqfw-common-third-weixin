package cn.jzyunqi.common.third.weixin.mp.mass;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.third.weixin.mp.callback.model.ReplyMsgData;
import cn.jzyunqi.common.third.weixin.mp.mass.model.MassRsp;
import cn.jzyunqi.common.third.weixin.mp.token.WxMpTokenApi;
import jakarta.annotation.Resource;

/**
 * @author wiiyaya
 * @since 2025/9/12
 */
public class WxMpMassApi {

    @Resource
    private WxMpTokenApi wxMpTokenApi;

    @Resource
    private WxMpMassApiProxy wxMpMassApiProxy;

    //基础消息能力 - 群发接口 - 根据标签进行群发
    public MassRsp massGroupMessageSend(String wxMpAppId, ReplyMsgData request, boolean preview) throws BusinessException {
        if (preview) {
            return wxMpMassApiProxy.massMessagePreview(wxMpTokenApi.getClientToken(wxMpAppId), request);
        } else {
            return wxMpMassApiProxy.massGroupMessageSend(wxMpTokenApi.getClientToken(wxMpAppId), request);
        }
    }
}
