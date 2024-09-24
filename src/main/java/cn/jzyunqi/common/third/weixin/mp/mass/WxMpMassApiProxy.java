package cn.jzyunqi.common.third.weixin.mp.mass;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.third.weixin.common.WxHttpExchange;
import cn.jzyunqi.common.third.weixin.mp.callback.model.ReplyMsgData;
import cn.jzyunqi.common.third.weixin.mp.mass.model.MassRsp;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

/**
 * @author wiiyaya
 * @since 2024/9/24
 */
@WxHttpExchange
@HttpExchange(url = "https://api.weixin.qq.com", accept = {"application/json"})
public interface WxMpMassApiProxy {

    //基础消息能力 - 群发接口 - 预览接口
    @GetExchange(url = "/cgi-bin/message/mass/preview")
    MassRsp massMessagePreview(@RequestParam String access_token, @RequestBody ReplyMsgData request) throws BusinessException;

    //基础消息能力 - 群发接口 - 根据标签进行群发
    @GetExchange(url = "/cgi-bin/message/mass/sendall")
    MassRsp massGroupMessageSend(@RequestParam String access_token, @RequestBody ReplyMsgData request) throws BusinessException;

}
