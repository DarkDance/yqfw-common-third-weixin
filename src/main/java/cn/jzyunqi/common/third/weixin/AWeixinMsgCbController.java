package cn.jzyunqi.common.third.weixin;

import cn.jzyunqi.common.third.weixin.client.WeixinCgiClient;
import cn.jzyunqi.common.third.weixin.model.callback.MsgDetailCb;
import cn.jzyunqi.common.third.weixin.model.callback.MsgSimpleCb;
import cn.jzyunqi.common.third.weixin.model.callback.item.EventMsgData;
import cn.jzyunqi.common.third.weixin.model.callback.item.ImageMsgData;
import cn.jzyunqi.common.third.weixin.model.callback.item.LinkMsgData;
import cn.jzyunqi.common.third.weixin.model.callback.item.LocationMsgData;
import cn.jzyunqi.common.third.weixin.model.callback.item.MiniProgramPageMsgData;
import cn.jzyunqi.common.third.weixin.model.callback.item.MpNewsMsgData;
import cn.jzyunqi.common.third.weixin.model.callback.item.MusicMsgData;
import cn.jzyunqi.common.third.weixin.model.callback.item.NewsMsgData;
import cn.jzyunqi.common.third.weixin.model.callback.item.ShortVideoMsgData;
import cn.jzyunqi.common.third.weixin.model.callback.item.TextMsgData;
import cn.jzyunqi.common.third.weixin.model.callback.item.ThumbMsgData;
import cn.jzyunqi.common.third.weixin.model.callback.item.TransferCustomerServiceMsgData;
import cn.jzyunqi.common.third.weixin.model.callback.item.VideoMsgData;
import cn.jzyunqi.common.third.weixin.model.callback.item.VoiceMsgData;
import cn.jzyunqi.common.third.weixin.model.callback.item.WxCardMsgData;
import cn.jzyunqi.common.third.weixin.model.request.ReplyMsgParam;
import cn.jzyunqi.common.third.weixin.utils.WeixinMsgUtilPlus;
import cn.jzyunqi.common.utils.BeanUtilPlus;
import cn.jzyunqi.common.utils.CurrentUserUtils;
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
public abstract class AWeixinMsgCbController {

    private static final String NOT_SUPPORT = "Not support now!";

    @Resource
    private WeixinCgiClient weixinCgiClient;

    /***
     * 微信小程序消息回调
     *
     * @param msgSimpleCb url参数
     * @param msgDetailCb 消息详细
     * @return 根据情况返回回调数据或"failed"或"success"
     */
    @RequestMapping
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
        return weixinCgiClient.replyMessageNotice(msgSimpleCb, msgDetailCb, decryptNotice -> {
            switch (decryptNotice.getMsgType()) {
                case text -> {return this.processTextMsg(BeanUtilPlus.copyAs(decryptNotice, TextMsgData.class));}
                case image -> {return this.processImageMsg(BeanUtilPlus.copyAs(decryptNotice, ImageMsgData.class));}
                case voice -> {return this.processVoiceMsg(BeanUtilPlus.copyAs(decryptNotice, VoiceMsgData.class));}
                case video -> {return this.processVideoMsg(BeanUtilPlus.copyAs(decryptNotice, VideoMsgData.class));}
                case thumb -> {return this.processThumbMsg(BeanUtilPlus.copyAs(decryptNotice, ThumbMsgData.class));}
                case shortvideo -> {return this.processShortVideoMsg(BeanUtilPlus.copyAs(decryptNotice, ShortVideoMsgData.class));}
                case music -> {return this.processMusicMsg(BeanUtilPlus.copyAs(decryptNotice, MusicMsgData.class));}
                case news -> {return this.processNewsMsg(BeanUtilPlus.copyAs(decryptNotice, NewsMsgData.class));}
                case mpnews -> {return this.processMpNewsMsg(BeanUtilPlus.copyAs(decryptNotice, MpNewsMsgData.class));}
                case wxcard -> {return this.processWxCardMsg(BeanUtilPlus.copyAs(decryptNotice, WxCardMsgData.class));}
                case location -> {return this.processLocationMsg(BeanUtilPlus.copyAs(decryptNotice, LocationMsgData.class));}
                case link -> {return this.processLinkMsg(BeanUtilPlus.copyAs(decryptNotice, LinkMsgData.class));}
                case event -> {return this.processEventMsg(BeanUtilPlus.copyAs(decryptNotice, EventMsgData.class));}
                case transfer_customer_service -> {return this.processTransferCustomerServiceMsg(BeanUtilPlus.copyAs(decryptNotice, TransferCustomerServiceMsgData.class));}
                case miniprogrampage -> {return this.processMiniProgramPageMsg(BeanUtilPlus.copyAs(decryptNotice, MiniProgramPageMsgData.class));}
                default -> {return null;}
            }
        });
    }

    protected ReplyMsgParam processMiniProgramPageMsg(MiniProgramPageMsgData miniProgramPageMsgData) {
        return WeixinMsgUtilPlus.prepareTextReply(miniProgramPageMsgData.getToUserName(), miniProgramPageMsgData.getFromUserName(), NOT_SUPPORT);
    }

    protected ReplyMsgParam processTransferCustomerServiceMsg(TransferCustomerServiceMsgData transferCustomerServiceMsgData) {
        return WeixinMsgUtilPlus.prepareTextReply(transferCustomerServiceMsgData.getToUserName(), transferCustomerServiceMsgData.getFromUserName(), NOT_SUPPORT);
    }

    protected ReplyMsgParam processEventMsg(EventMsgData eventMsgData) {
        return WeixinMsgUtilPlus.prepareTextReply(eventMsgData.getToUserName(), eventMsgData.getFromUserName(), NOT_SUPPORT);
    }

    protected ReplyMsgParam processLinkMsg(LinkMsgData linkMsgData) {
        return WeixinMsgUtilPlus.prepareTextReply(linkMsgData.getToUserName(), linkMsgData.getFromUserName(), NOT_SUPPORT);
    }

    protected ReplyMsgParam processLocationMsg(LocationMsgData locationMsgData) {
        return WeixinMsgUtilPlus.prepareTextReply(locationMsgData.getToUserName(), locationMsgData.getFromUserName(), NOT_SUPPORT);
    }

    protected ReplyMsgParam processWxCardMsg(WxCardMsgData wxCardMsgData) {
        return WeixinMsgUtilPlus.prepareTextReply(wxCardMsgData.getToUserName(), wxCardMsgData.getFromUserName(), NOT_SUPPORT);
    }

    protected ReplyMsgParam processMpNewsMsg(MpNewsMsgData mpNewsMsgData) {
        return WeixinMsgUtilPlus.prepareTextReply(mpNewsMsgData.getToUserName(), mpNewsMsgData.getFromUserName(), NOT_SUPPORT);
    }

    protected ReplyMsgParam processNewsMsg(NewsMsgData newsMsgData) {
        return WeixinMsgUtilPlus.prepareTextReply(newsMsgData.getToUserName(), newsMsgData.getFromUserName(), NOT_SUPPORT);
    }

    protected ReplyMsgParam processMusicMsg(MusicMsgData musicMsgData) {
        return WeixinMsgUtilPlus.prepareTextReply(musicMsgData.getToUserName(), musicMsgData.getFromUserName(), NOT_SUPPORT);
    }

    private ReplyMsgParam processShortVideoMsg(ShortVideoMsgData shortVideoMsgData) {
        return WeixinMsgUtilPlus.prepareTextReply(shortVideoMsgData.getToUserName(), shortVideoMsgData.getFromUserName(), NOT_SUPPORT);
    }

    private ReplyMsgParam processThumbMsg(ThumbMsgData thumbMsgData) {
        return WeixinMsgUtilPlus.prepareTextReply(thumbMsgData.getToUserName(), thumbMsgData.getFromUserName(), NOT_SUPPORT);
    }

    protected ReplyMsgParam processVideoMsg(VideoMsgData videoMsgData) {
        return WeixinMsgUtilPlus.prepareTextReply(videoMsgData.getToUserName(), videoMsgData.getFromUserName(), NOT_SUPPORT);
    }

    protected ReplyMsgParam processVoiceMsg(VoiceMsgData voiceMsgData) {
        return WeixinMsgUtilPlus.prepareTextReply(voiceMsgData.getToUserName(), voiceMsgData.getFromUserName(), NOT_SUPPORT);
    }

    protected ReplyMsgParam processImageMsg(ImageMsgData imageMsgData) {
        return WeixinMsgUtilPlus.prepareTextReply(imageMsgData.getToUserName(), imageMsgData.getFromUserName(), NOT_SUPPORT);
    }

    protected ReplyMsgParam processTextMsg(TextMsgData textMsgCb) {
        return WeixinMsgUtilPlus.prepareTextReply(textMsgCb.getToUserName(), textMsgCb.getFromUserName(), NOT_SUPPORT);
    }
}
