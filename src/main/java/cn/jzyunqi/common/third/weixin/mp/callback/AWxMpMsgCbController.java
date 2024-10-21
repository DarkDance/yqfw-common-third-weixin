package cn.jzyunqi.common.third.weixin.mp.callback;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.third.weixin.mp.WxMpClient;
import cn.jzyunqi.common.third.weixin.mp.callback.model.EventMsgData;
import cn.jzyunqi.common.third.weixin.mp.callback.model.ImageMsgData;
import cn.jzyunqi.common.third.weixin.mp.callback.model.LinkMsgData;
import cn.jzyunqi.common.third.weixin.mp.callback.model.LocationEventData;
import cn.jzyunqi.common.third.weixin.mp.callback.model.LocationMsgData;
import cn.jzyunqi.common.third.weixin.mp.callback.model.MemberCardEventData;
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
    public Object userMessageCallback(MsgSimpleCb msgSimpleCb, @RequestBody(required = false) MsgDetailCb msgDetailCb, @RequestBody(required = false) String msgDetailCbStr, @RequestHeader Map<String, String[]> headers) {
        log.debug("""

                        ======Request Header    : {}
                        ======Request Params    : {}
                        ======Request BodyStr   : {}
                        """,
                headers,
                msgSimpleCb,
                msgDetailCbStr
        );
        return wxMpClient.cb.replyMessageNotice(msgSimpleCb, msgDetailCb, decryptNotice ->
                switch (decryptNotice.getMsgType()) {
                    case text -> this.processTextMsg(BeanUtilPlus.copyAs(decryptNotice, TextMsgData.class));
                    case image -> this.processImageMsg(BeanUtilPlus.copyAs(decryptNotice, ImageMsgData.class));
                    case voice -> this.processVoiceMsg(BeanUtilPlus.copyAs(decryptNotice, VoiceMsgData.class));
                    case video -> this.processVideoMsg(BeanUtilPlus.copyAs(decryptNotice, VideoMsgData.class));
                    case thumb -> this.processThumbMsg(BeanUtilPlus.copyAs(decryptNotice, ThumbMsgData.class));
                    case shortvideo ->
                            this.processShortVideoMsg(BeanUtilPlus.copyAs(decryptNotice, ShortVideoMsgData.class));
                    case music -> this.processMusicMsg(BeanUtilPlus.copyAs(decryptNotice, MusicMsgData.class));
                    case news -> this.processNewsMsg(BeanUtilPlus.copyAs(decryptNotice, NewsMsgData.class));
                    case mpnews -> this.processMpNewsMsg(BeanUtilPlus.copyAs(decryptNotice, MpNewsMsgData.class));
                    case wxcard -> this.processWxCardMsg(BeanUtilPlus.copyAs(decryptNotice, WxCardMsgData.class));
                    case location -> this.processLocationMsg(BeanUtilPlus.copyAs(decryptNotice, LocationMsgData.class));
                    case link -> this.processLinkMsg(BeanUtilPlus.copyAs(decryptNotice, LinkMsgData.class));
                    case event -> switch (decryptNotice.getEvent()) {
                        case subscribe ->
                                this.processSubscribeEvent(BeanUtilPlus.copyAs(decryptNotice, EventMsgData.class));
                        case unsubscribe ->
                                this.processUnsubscribeEvent(BeanUtilPlus.copyAs(decryptNotice, EventMsgData.class));
                        case LOCATION ->
                                this.processLocationEvent(BeanUtilPlus.copyAs(decryptNotice, LocationEventData.class));
                        case CLICK -> this.processClickEvent(BeanUtilPlus.copyAs(decryptNotice, EventMsgData.class));
                        case VIEW -> this.processViewEvent(BeanUtilPlus.copyAs(decryptNotice, EventMsgData.class));
                        case scancode_push ->
                                this.processScancodePushEvent(BeanUtilPlus.copyAs(decryptNotice, EventMsgData.class));
                        case scancode_waitmsg ->
                                this.processScancodeWaitMsgEvent(BeanUtilPlus.copyAs(decryptNotice, EventMsgData.class));
                        case pic_sysphoto ->
                                this.processPicSysphotoEvent(BeanUtilPlus.copyAs(decryptNotice, EventMsgData.class));
                        case pic_photo_or_album ->
                                this.processPicPhotoOrAlbumEvent(BeanUtilPlus.copyAs(decryptNotice, EventMsgData.class));
                        case pic_weixin ->
                                this.processPicWeixinEvent(BeanUtilPlus.copyAs(decryptNotice, EventMsgData.class));
                        case location_select ->
                                this.processLocationSelectEvent(BeanUtilPlus.copyAs(decryptNotice, EventMsgData.class));
                        case TEMPLATESENDJOBFINISH ->
                                this.processTemplateSendJobFinishEvent(BeanUtilPlus.copyAs(decryptNotice, EventMsgData.class));
                        case card_pass_check ->
                                this.processCardPassCheckEvent(BeanUtilPlus.copyAs(decryptNotice, EventMsgData.class));
                        case card_not_pass_check ->
                                this.processCardNotPassCheckEvent(BeanUtilPlus.copyAs(decryptNotice, EventMsgData.class));
                        case user_gifting_card ->
                                this.processUserGiftingCardEvent(BeanUtilPlus.copyAs(decryptNotice, EventMsgData.class));
                        case user_del_card ->
                                this.processUserDelCardEvent(BeanUtilPlus.copyAs(decryptNotice, EventMsgData.class));
                        case user_pay_from_pay_cell ->
                                this.processUserPayFromPayCellEvent(BeanUtilPlus.copyAs(decryptNotice, EventMsgData.class));
                        case user_enter_session_from_card ->
                                this.processUserEnterSessionFromCardEvent(BeanUtilPlus.copyAs(decryptNotice, EventMsgData.class));
                        case update_member_card ->
                                this.processUpdateMemberCardEvent(BeanUtilPlus.copyAs(decryptNotice, EventMsgData.class));
                        case card_sku_remind ->
                                this.processCardSkuRemindEvent(BeanUtilPlus.copyAs(decryptNotice, EventMsgData.class));
                        case card_pay_order ->
                                this.processCardPayOrderEvent(BeanUtilPlus.copyAs(decryptNotice, EventMsgData.class));
                        case submit_membercard_user_info ->
                                this.processSubmitMemberCardEvent(BeanUtilPlus.copyAs(decryptNotice, MemberCardEventData.class));
                        case user_get_card ->
                                this.processUserGetCardEvent(BeanUtilPlus.copyAs(decryptNotice, MemberCardEventData.class));
                        case user_consume_card ->
                                this.processUserConsumeCardEvent(BeanUtilPlus.copyAs(decryptNotice, EventMsgData.class));
                        case user_view_card ->
                                this.processUserViewCardEvent(BeanUtilPlus.copyAs(decryptNotice, EventMsgData.class));
                    };
                    case transfer_customer_service ->
                            this.processTransferCustomerServiceMsg(BeanUtilPlus.copyAs(decryptNotice, TransferCustomerServiceMsgData.class));
                    case miniprogrampage ->
                            this.processMiniProgramPageMsg(BeanUtilPlus.copyAs(decryptNotice, MiniProgramPageMsgData.class));
                }
        );
    }

    protected ReplyMsgData processUserViewCardEvent(EventMsgData eventMsgData) throws BusinessException {
        return null;
    }

    protected ReplyMsgData processUserConsumeCardEvent(EventMsgData eventMsgData) throws BusinessException {
        return null;
    }

    protected ReplyMsgData processUserGetCardEvent(MemberCardEventData eventMsgData) throws BusinessException {
        return null;
    }

    protected ReplyMsgData processCardPayOrderEvent(EventMsgData eventMsgData) throws BusinessException {
        return null;
    }

    protected ReplyMsgData processCardSkuRemindEvent(EventMsgData eventMsgData) throws BusinessException {
        return null;
    }

    protected ReplyMsgData processUpdateMemberCardEvent(EventMsgData eventMsgData) throws BusinessException {
        return null;
    }

    protected ReplyMsgData processUserEnterSessionFromCardEvent(EventMsgData eventMsgData) throws BusinessException {
        return null;
    }

    protected ReplyMsgData processUserPayFromPayCellEvent(EventMsgData eventMsgData) throws BusinessException {
        return null;
    }

    protected ReplyMsgData processUserDelCardEvent(EventMsgData eventMsgData) throws BusinessException {
        return null;
    }

    protected ReplyMsgData processUserGiftingCardEvent(EventMsgData eventMsgData) throws BusinessException {
        return null;
    }

    protected ReplyMsgData processCardNotPassCheckEvent(EventMsgData eventMsgData) throws BusinessException {
        return null;
    }

    protected ReplyMsgData processCardPassCheckEvent(EventMsgData eventMsgData) throws BusinessException {
        return null;
    }

    protected ReplyMsgData processSubmitMemberCardEvent(MemberCardEventData eventMsgData) throws BusinessException {
        return null;
    }

    protected ReplyMsgData processTemplateSendJobFinishEvent(EventMsgData eventMsgData) throws BusinessException {
        return null;
    }

    protected ReplyMsgData processLocationSelectEvent(EventMsgData eventMsgData) throws BusinessException {
        return null;
    }

    protected ReplyMsgData processPicWeixinEvent(EventMsgData eventMsgData) throws BusinessException {
        return null;
    }

    protected ReplyMsgData processPicPhotoOrAlbumEvent(EventMsgData eventMsgData) throws BusinessException {
        return null;
    }

    protected ReplyMsgData processPicSysphotoEvent(EventMsgData eventMsgData) throws BusinessException {
        return null;
    }

    protected ReplyMsgData processScancodeWaitMsgEvent(EventMsgData eventMsgData) throws BusinessException {
        return null;
    }

    protected ReplyMsgData processScancodePushEvent(EventMsgData eventMsgData) throws BusinessException {
        return null;
    }

    protected ReplyMsgData processViewEvent(EventMsgData eventMsgData) throws BusinessException {
        return null;
    }

    protected ReplyMsgData processClickEvent(EventMsgData eventMsgData) throws BusinessException {
        return null;
    }

    protected ReplyMsgData processLocationEvent(LocationEventData eventMsgData) throws BusinessException {
        return null;
    }

    protected ReplyMsgData processUnsubscribeEvent(EventMsgData eventMsgData) throws BusinessException {
        return null;
    }

    protected ReplyMsgData processSubscribeEvent(EventMsgData eventMsgData) throws BusinessException {
        return null;
    }

    protected ReplyMsgData processMiniProgramPageMsg(MiniProgramPageMsgData miniProgramPageMsgData) throws BusinessException {
        return WxMsgUtilPlus.prepareTextReply(miniProgramPageMsgData.getToUserName(), miniProgramPageMsgData.getFromUserName(), NOT_SUPPORT);
    }

    protected ReplyMsgData processTransferCustomerServiceMsg(TransferCustomerServiceMsgData transferCustomerServiceMsgData) throws BusinessException {
        return WxMsgUtilPlus.prepareTextReply(transferCustomerServiceMsgData.getToUserName(), transferCustomerServiceMsgData.getFromUserName(), NOT_SUPPORT);
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
