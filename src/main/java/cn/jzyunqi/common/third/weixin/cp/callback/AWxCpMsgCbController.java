package cn.jzyunqi.common.third.weixin.cp.callback;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.third.weixin.cp.WxCpAuth;
import cn.jzyunqi.common.third.weixin.cp.WxCpAuthHelper;
import cn.jzyunqi.common.third.weixin.cp.WxCpClient;
import cn.jzyunqi.common.third.weixin.mp.WxMpAuth;
import cn.jzyunqi.common.third.weixin.mp.WxMpClient;
import cn.jzyunqi.common.third.weixin.mp.callback.WxMsgCbHelper;
import cn.jzyunqi.common.third.weixin.mp.callback.WxMsgUtilPlus;
import cn.jzyunqi.common.third.weixin.mp.callback.model.EventMsgData;
import cn.jzyunqi.common.third.weixin.mp.callback.model.ImageMsgData;
import cn.jzyunqi.common.third.weixin.mp.callback.model.LinkMsgData;
import cn.jzyunqi.common.third.weixin.mp.callback.model.LocationEventData;
import cn.jzyunqi.common.third.weixin.mp.callback.model.LocationMsgData;
import cn.jzyunqi.common.third.weixin.mp.callback.model.MemberCardEventData;
import cn.jzyunqi.common.third.weixin.mp.callback.model.MenuMsgData;
import cn.jzyunqi.common.third.weixin.mp.callback.model.MiniProgramPageMsgData;
import cn.jzyunqi.common.third.weixin.mp.callback.model.MpNewsMsgData;
import cn.jzyunqi.common.third.weixin.mp.callback.model.MsgDetailCb;
import cn.jzyunqi.common.third.weixin.mp.callback.model.MsgSimpleCb;
import cn.jzyunqi.common.third.weixin.mp.callback.model.MusicMsgData;
import cn.jzyunqi.common.third.weixin.mp.callback.model.NewsMsgData;
import cn.jzyunqi.common.third.weixin.mp.callback.model.ReplyMsgData;
import cn.jzyunqi.common.third.weixin.mp.callback.model.ShortVideoMsgData;
import cn.jzyunqi.common.third.weixin.mp.callback.model.TextMsgData;
import cn.jzyunqi.common.third.weixin.mp.callback.model.ThumbMsgData;
import cn.jzyunqi.common.third.weixin.mp.callback.model.TransferCustomerServiceMsgData;
import cn.jzyunqi.common.third.weixin.mp.callback.model.VideoMsgData;
import cn.jzyunqi.common.third.weixin.mp.callback.model.VoiceMsgData;
import cn.jzyunqi.common.third.weixin.mp.callback.model.WxCardMsgData;
import cn.jzyunqi.common.utils.BeanUtilPlus;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * @author wiiyaya
 * @since 2025/11/17
 */
@Slf4j
public class AWxCpMsgCbController {

    private static final String NOT_SUPPORT = "Not support now!";

    @Resource //供子类使用
    protected WxCpClient wxCpClient;

    @Resource
    private WxCpAuthHelper wxCpAuthHelper;

    /***
     * 微信公众号消息回调
     *
     * @param msgSimpleCb url参数
     * @param msgDetailCb 消息详细
     * @return 根据情况返回回调数据或"failed"或"success"
     */
    @RequestMapping(consumes = "text/xml", produces = "text/xml")
    @ResponseBody
    public Object userMessageCallback(@PathVariable String appId, MsgSimpleCb msgSimpleCb, @RequestBody(required = false) MsgDetailCb msgDetailCb, @RequestBody(required = false) String msgDetailCbStr, @RequestHeader Map<String, String[]> headers) {
        log.debug("""

                        ======Request Header    : {}
                        ======Request AppId     : {}
                        ======Request Params    : {}
                        ======Request BodyStr   : {}
                        """,
                headers,
                appId,
                msgSimpleCb,
                msgDetailCbStr
        );
        WxCpAuth wxMpAuth = wxCpAuthHelper.chooseWxCpAuth(appId);
        return WxMsgCbHelper.replyMessageNotice(appId, wxMpAuth.getVerificationToken(), wxMpAuth.getEncryptKey(), msgSimpleCb, msgDetailCb, decryptNotice ->
                switch (decryptNotice.getMsgType()) {
                    case text -> this.processTextMsg(BeanUtilPlus.copyAs(decryptNotice, TextMsgData.class));
                    default -> this.processTextMsg(BeanUtilPlus.copyAs(decryptNotice, TextMsgData.class));
                }
        );
    }

    protected ReplyMsgData processTextMsg(TextMsgData textMsgCb) throws BusinessException {
        return WxMsgUtilPlus.prepareTextReply(textMsgCb.getToUserName(), textMsgCb.getFromUserName(), NOT_SUPPORT);
    }
}
