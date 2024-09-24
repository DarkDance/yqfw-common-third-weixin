package cn.jzyunqi.common.third.weixin.mp.callback;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.third.weixin.mp.WeixinCgiClient;
import cn.jzyunqi.common.third.weixin.mp.WxMpClient;
import cn.jzyunqi.common.third.weixin.mp.WxMpConfig;
import cn.jzyunqi.common.third.weixin.mp.callback.model.MsgDetailCb;
import cn.jzyunqi.common.third.weixin.mp.callback.model.MsgSimpleCb;
import cn.jzyunqi.common.third.weixin.mp.callback.model.EventMsgData;
import cn.jzyunqi.common.third.weixin.mp.callback.model.ImageMsgData;
import cn.jzyunqi.common.third.weixin.mp.callback.model.LinkMsgData;
import cn.jzyunqi.common.third.weixin.mp.callback.model.LocationMsgData;
import cn.jzyunqi.common.third.weixin.mp.callback.model.MiniProgramPageMsgData;
import cn.jzyunqi.common.third.weixin.mp.callback.model.MpNewsMsgData;
import cn.jzyunqi.common.third.weixin.mp.callback.model.MusicMsgData;
import cn.jzyunqi.common.third.weixin.mp.callback.model.NewsMsgData;
import cn.jzyunqi.common.third.weixin.mp.callback.model.ShortVideoMsgData;
import cn.jzyunqi.common.third.weixin.mp.callback.model.TextMsgData;
import cn.jzyunqi.common.third.weixin.mp.callback.model.ThumbMsgData;
import cn.jzyunqi.common.third.weixin.mp.callback.model.TransferCustomerServiceMsgData;
import cn.jzyunqi.common.third.weixin.mp.callback.model.VideoMsgData;
import cn.jzyunqi.common.third.weixin.mp.callback.model.VoiceMsgData;
import cn.jzyunqi.common.third.weixin.mp.callback.model.WxCardMsgData;
import cn.jzyunqi.common.third.weixin.mp.callback.model.ReplyMsgData;
import cn.jzyunqi.common.utils.BeanUtilPlus;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * 微信回调接口
 *
 * @author wiiyaya
 * @since 2021/5/9.
 */
@Slf4j
public abstract class AWxMpMsgCbController {

    private static final String NOT_SUPPORT = "Not support now!";

    @Resource
    protected WxMpClient wxMpClient;


    /***
     * 微信小程序消息回调
     *
     * @param msgSimpleCb url参数
     * @param msgDetailCb 消息详细
     * @return 根据情况返回回调数据或"failed"或"success"
     */
    @RequestMapping(consumes = "text/xml", produces = "text/xml")
    @ResponseBody
    public Object userMessageCallback(MsgSimpleCb msgSimpleCb, @RequestBody(required = false) MsgDetailCb msgDetailCb, @RequestHeader Map<String, String[]> headers) {
        log.debug("""

                        ======Request Header : {}
                        ======Request Params : {}
                        ======Request Body   : {}
                        """,
                headers,
                msgSimpleCb,
                msgDetailCb
        );
        return wxMpClient.cb.replyMessageNotice(msgSimpleCb, msgDetailCb, decryptNotice -> {
            switch (decryptNotice.getMsgType()) {
                case text -> {
                    return this.processTextMsg(BeanUtilPlus.copyAs(decryptNotice, TextMsgData.class));
                }
                case image -> {
                    return this.processImageMsg(BeanUtilPlus.copyAs(decryptNotice, ImageMsgData.class));
                }
                case voice -> {
                    return this.processVoiceMsg(BeanUtilPlus.copyAs(decryptNotice, VoiceMsgData.class));
                }
                case video -> {
                    return this.processVideoMsg(BeanUtilPlus.copyAs(decryptNotice, VideoMsgData.class));
                }
                case thumb -> {
                    return this.processThumbMsg(BeanUtilPlus.copyAs(decryptNotice, ThumbMsgData.class));
                }
                case shortvideo -> {
                    return this.processShortVideoMsg(BeanUtilPlus.copyAs(decryptNotice, ShortVideoMsgData.class));
                }
                case music -> {
                    return this.processMusicMsg(BeanUtilPlus.copyAs(decryptNotice, MusicMsgData.class));
                }
                case news -> {
                    return this.processNewsMsg(BeanUtilPlus.copyAs(decryptNotice, NewsMsgData.class));
                }
                case mpnews -> {
                    return this.processMpNewsMsg(BeanUtilPlus.copyAs(decryptNotice, MpNewsMsgData.class));
                }
                case wxcard -> {
                    return this.processWxCardMsg(BeanUtilPlus.copyAs(decryptNotice, WxCardMsgData.class));
                }
                case location -> {
                    return this.processLocationMsg(BeanUtilPlus.copyAs(decryptNotice, LocationMsgData.class));
                }
                case link -> {
                    return this.processLinkMsg(BeanUtilPlus.copyAs(decryptNotice, LinkMsgData.class));
                }
                case event -> {
                    return this.processEventMsg(BeanUtilPlus.copyAs(decryptNotice, EventMsgData.class));
                }
                case transfer_customer_service -> {
                    return this.processTransferCustomerServiceMsg(BeanUtilPlus.copyAs(decryptNotice, TransferCustomerServiceMsgData.class));
                }
                case miniprogrampage -> {
                    return this.processMiniProgramPageMsg(BeanUtilPlus.copyAs(decryptNotice, MiniProgramPageMsgData.class));
                }
                default -> {
                    return null;
                }
            }
        });
    }

    protected ReplyMsgData processMiniProgramPageMsg(MiniProgramPageMsgData miniProgramPageMsgData) throws BusinessException {
        return WxMsgUtilPlus.prepareTextReply(miniProgramPageMsgData.getToUserName(), miniProgramPageMsgData.getFromUserName(), NOT_SUPPORT);
    }

    protected ReplyMsgData processTransferCustomerServiceMsg(TransferCustomerServiceMsgData transferCustomerServiceMsgData) throws BusinessException {
        return WxMsgUtilPlus.prepareTextReply(transferCustomerServiceMsgData.getToUserName(), transferCustomerServiceMsgData.getFromUserName(), NOT_SUPPORT);
    }

    protected ReplyMsgData processEventMsg(EventMsgData eventMsgData) throws BusinessException {
        return WxMsgUtilPlus.prepareTextReply(eventMsgData.getToUserName(), eventMsgData.getFromUserName(), NOT_SUPPORT);
    }

    protected ReplyMsgData processLinkMsg(LinkMsgData linkMsgData) throws BusinessException {
        return WxMsgUtilPlus.prepareTextReply(linkMsgData.getToUserName(), linkMsgData.getFromUserName(), NOT_SUPPORT);
    }

    protected ReplyMsgData processLocationMsg(LocationMsgData locationMsgData) throws BusinessException {
        return WxMsgUtilPlus.prepareTextReply(locationMsgData.getToUserName(), locationMsgData.getFromUserName(), NOT_SUPPORT);
    }

    protected ReplyMsgData processWxCardMsg(WxCardMsgData wxCardMsgData) throws BusinessException {
        return WxMsgUtilPlus.prepareTextReply(wxCardMsgData.getToUserName(), wxCardMsgData.getFromUserName(), NOT_SUPPORT);
    }

    protected ReplyMsgData processMpNewsMsg(MpNewsMsgData mpNewsMsgData) throws BusinessException {
        return WxMsgUtilPlus.prepareTextReply(mpNewsMsgData.getToUserName(), mpNewsMsgData.getFromUserName(), NOT_SUPPORT);
    }

    protected ReplyMsgData processNewsMsg(NewsMsgData newsMsgData) throws BusinessException {
        return WxMsgUtilPlus.prepareTextReply(newsMsgData.getToUserName(), newsMsgData.getFromUserName(), NOT_SUPPORT);
    }

    protected ReplyMsgData processMusicMsg(MusicMsgData musicMsgData) throws BusinessException {
        return WxMsgUtilPlus.prepareTextReply(musicMsgData.getToUserName(), musicMsgData.getFromUserName(), NOT_SUPPORT);
    }

    private ReplyMsgData processShortVideoMsg(ShortVideoMsgData shortVideoMsgData) throws BusinessException {
        return WxMsgUtilPlus.prepareTextReply(shortVideoMsgData.getToUserName(), shortVideoMsgData.getFromUserName(), NOT_SUPPORT);
    }

    private ReplyMsgData processThumbMsg(ThumbMsgData thumbMsgData) throws BusinessException {
        return WxMsgUtilPlus.prepareTextReply(thumbMsgData.getToUserName(), thumbMsgData.getFromUserName(), NOT_SUPPORT);
    }

    protected ReplyMsgData processVideoMsg(VideoMsgData videoMsgData) throws BusinessException {
        return WxMsgUtilPlus.prepareTextReply(videoMsgData.getToUserName(), videoMsgData.getFromUserName(), NOT_SUPPORT);
    }

    protected ReplyMsgData processVoiceMsg(VoiceMsgData voiceMsgData) throws BusinessException {
        return WxMsgUtilPlus.prepareTextReply(voiceMsgData.getToUserName(), voiceMsgData.getFromUserName(), NOT_SUPPORT);
    }

    protected ReplyMsgData processImageMsg(ImageMsgData imageMsgData) throws BusinessException {
        return WxMsgUtilPlus.prepareTextReply(imageMsgData.getToUserName(), imageMsgData.getFromUserName(), NOT_SUPPORT);
    }

    protected ReplyMsgData processTextMsg(TextMsgData textMsgCb) throws BusinessException {
        return WxMsgUtilPlus.prepareTextReply(textMsgCb.getToUserName(), textMsgCb.getFromUserName(), NOT_SUPPORT);
    }
}
