package cn.jzyunqi.common.third.weixin.mp.utils;

import cn.jzyunqi.common.third.weixin.mp.model.enums.MsgType;
import cn.jzyunqi.common.third.weixin.mp.model.request.item.ItemData;
import cn.jzyunqi.common.third.weixin.mp.model.request.ItemListParam;
import cn.jzyunqi.common.third.weixin.mp.model.request.ReplyMsgParam;
import cn.jzyunqi.common.utils.BooleanUtilPlus;
import cn.jzyunqi.common.utils.StringUtilPlus;

import jakarta.annotation.Nullable;
import java.util.List;

/**
 * @author wiiyaya
 * @since 2018/8/18.
 */
public class WeixinMsgUtilPlus {

    /**
     * 准备文本回复消息
     *
     * @param fromUserName 发送者
     * @param toOpenId     接收者
     * @param content      内容
     * @return 模型
     */
    public static ReplyMsgParam prepareTextReply(@Nullable String fromUserName, String toOpenId, String content){
        ReplyMsgParam textReply = new ReplyMsgParam();
        textReply.setToUserName(toOpenId);
        textReply.setFromUserName(fromUserName);
        textReply.setCreateTime((int) System.currentTimeMillis());
        textReply.setMsgType(MsgType.text);
        textReply.setContent(content);

        ItemData text = new ItemData();
        text.setContent(content);
        textReply.setText(text);
        return textReply;
    }

    /**
     * 准备图片回复消息
     *
     * @param fromUserName 发送者
     * @param toOpenId     接收者
     * @param mediaId      媒体ID
     * @return 模型
     */
    public static ReplyMsgParam prepareImageReply(@Nullable String fromUserName, String toOpenId, String mediaId){
        ReplyMsgParam imageReply = new ReplyMsgParam();
        imageReply.setToUserName(toOpenId);
        imageReply.setFromUserName(fromUserName);
        imageReply.setCreateTime((int) System.currentTimeMillis());
        imageReply.setMsgType(MsgType.image);

        ItemData image = new ItemData();
        image.setMediaId(mediaId);
        imageReply.setImage(image);
        return imageReply;
    }

    /**
     * 准备语音回复消息
     *
     * @param fromUserName 发送者
     * @param toOpenId     接收者
     * @param mediaId      媒体ID
     * @return 模型
     */
    public static ReplyMsgParam prepareVoiceReply(@Nullable String fromUserName, String toOpenId, String mediaId){
        ReplyMsgParam voiceReply = new ReplyMsgParam();
        voiceReply.setToUserName(toOpenId);
        voiceReply.setFromUserName(fromUserName);
        voiceReply.setCreateTime((int) System.currentTimeMillis());
        voiceReply.setMsgType(MsgType.voice);

        ItemData voice = new ItemData();
        voice.setMediaId(mediaId);
        voiceReply.setVoice(voice);
        return voiceReply;
    }

    /**
     * 准备视频回复消息
     *
     * @param fromUserName 发送者
     * @param toOpenId     接收者
     * @param title        视频标题
     * @param description  视频描述
     * @param mediaId      媒体ID
     * @param thumbMediaId 缩略图的媒体ID
     * @return 模型
     */
    public static ReplyMsgParam prepareVideoReply(@Nullable String fromUserName, String toOpenId, String title, String description, String mediaId, @Nullable String thumbMediaId){
        ReplyMsgParam videoReply = new ReplyMsgParam();
        videoReply.setToUserName(toOpenId);
        videoReply.setFromUserName(fromUserName);
        videoReply.setCreateTime((int) System.currentTimeMillis());
        videoReply.setMsgType(MsgType.video);

        ItemData video = new ItemData();
        video.setTitle(title);
        video.setDescription(description);
        video.setMediaId(mediaId);
        video.setThumbMediaId(thumbMediaId);
        videoReply.setVideo(video);
        return videoReply;
    }

    /**
     * 准备音乐回复消息
     *
     * @param fromUserName 发送者
     * @param toOpenId     接收者
     * @param title        音乐标题
     * @param description  音乐描述
     * @param musicUrl     音乐地址
     * @param hqMusicUrl   高清地址
     * @param thumbMediaId 媒体ID
     * @return 模型
     */
    public static ReplyMsgParam prepareMusicReply(@Nullable String fromUserName, String toOpenId, String title, String description, String musicUrl, String hqMusicUrl, String thumbMediaId){
        ReplyMsgParam musicReply = new ReplyMsgParam();
        musicReply.setToUserName(toOpenId);
        musicReply.setFromUserName(fromUserName);
        musicReply.setCreateTime((int) System.currentTimeMillis());
        musicReply.setMsgType(MsgType.music);

        ItemData music = new ItemData();
        music.setTitle(title);
        music.setDescription(description);
        music.setMusicUrl(musicUrl);
        music.setHqMusicUrl(hqMusicUrl);
        music.setThumbMediaId(thumbMediaId);
        musicReply.setMusic(music);
        return musicReply;
    }

    /**
     * 准备回复文章组件
     *
     * @param title       标题
     * @param description 描述
     * @param picUrl      图片链接
     * @param url         跳转链接
     * @return 模型
     */
    public static ItemData prepareArticleReplyItem(String title, String description, String picUrl, String url){
        ItemData itemData = new ItemData();
        itemData.setTitle(title);
        itemData.setDescription(description);
        itemData.setPicUrl(picUrl);
        itemData.setUrl(url);
        return itemData;
    }

    /**
     * 准备文章回复消息(点击跳转到外链)
     *
     * @param fromUserName     发送者
     * @param toOpenId         接收者
     * @param itemDataList 内容列表
     * @return 模型
     */
    public static ReplyMsgParam prepareArticlesReply(@Nullable String fromUserName, String toOpenId, List<ItemData> itemDataList){
        ReplyMsgParam articlesReply = new ReplyMsgParam();
        articlesReply.setToUserName(toOpenId);
        articlesReply.setFromUserName(fromUserName);
        articlesReply.setCreateTime((int) System.currentTimeMillis());
        articlesReply.setMsgType(MsgType.news);

        ItemListParam articleList = new ItemListParam();
        articleList.setArticles(itemDataList);

        articlesReply.setArticleCount(itemDataList.size());
        articlesReply.setArticles(articleList);
        return articlesReply;
    }

    /**
     * 准备文章回复消息(点击跳转到公众号文章)
     *
     * @param toOpenId  接收者
     * @param mediaId   媒体ID
     * @return 模型
     */
    public static ReplyMsgParam prepareMPArticlesReply(String toOpenId, String mediaId){
        ReplyMsgParam replyMsgParam = new ReplyMsgParam();
        replyMsgParam.setToUserName(toOpenId);
        replyMsgParam.setMsgType(MsgType.mpnews);

        ItemData mpNewsModel = new ItemData();
        mpNewsModel.setMediaId(mediaId);
        replyMsgParam.setMpArticles(mpNewsModel);
        return replyMsgParam;
    }

    /**
     * 准备素材文章组件
     *
     * @param title 标题
     * @param thumbMediaId 图文消息缩略图(如果是永久图文，则必须是永久mediaID，如果是原创图文，则必须是临时mediaId？)
     * @param content 内容
     * @param digest 描述
     * @param author 作者
     * @param contentSourceUrl 阅读原文链接
     * @param showCoverPic 	是否显示封面
     * @return 模型
     */
    public static ItemData prepareMaterialArticle(String title, String thumbMediaId, String content, String digest, String author, String contentSourceUrl, Boolean showCoverPic){
        ItemData itemData = new ItemData();
        itemData.setThumbMediaId(thumbMediaId);
        itemData.setAuthor(author);
        itemData.setTitle(title);
        itemData.setContent(content);
        itemData.setContentSourceUrl(contentSourceUrl);
        itemData.setDigest(digest);
        itemData.setShowCoverPic(BooleanUtilPlus.isTrue(showCoverPic) ? 1 : 0);
        return itemData;
    }

    /**
     * 准备原创文章消息
     *
     * @param itemDataList 内容列表
     * @return 模型
     */
    public static ItemListParam prepareMaterialArticlesReply(List<ItemData> itemDataList){
        ItemListParam itemListParam = new ItemListParam();
        itemListParam.setArticles(itemDataList);
        return itemListParam;
    }

    /**
     * 准备发送卡券消息
     *
     * @param toOpenId   消息接收方
     * @param cardId     code码
     */
    public static ReplyMsgParam prepareWxCardReply(String toOpenId, String cardId){
        ReplyMsgParam replyMsgParam = new ReplyMsgParam();
        replyMsgParam.setToUserName(toOpenId);
        replyMsgParam.setMsgType(MsgType.wxcard);

        ItemData wxCardModel = new ItemData();
        wxCardModel.setCardId(cardId);
        replyMsgParam.setWxCard(wxCardModel);
        return replyMsgParam;
    }

    /**
     * 准备转发到客服消息
     *
     * @param fromUserName 发送者
     * @param toOpenId     接收者
     * @param csAccount    客服账号(可以为空)
     * @return 模型
     */
    public static ReplyMsgParam prepareCSTransferReply(String fromUserName, String toOpenId, String csAccount){
        ReplyMsgParam reply = new ReplyMsgParam();
        reply.setToUserName(toOpenId);
        reply.setFromUserName(fromUserName);
        reply.setCreateTime((int) System.currentTimeMillis());
        reply.setMsgType(MsgType.transfer_customer_service);

        if (StringUtilPlus.isNotEmpty(csAccount)) {
            ItemData csAcc = new ItemData();
            csAcc.setKfAccount(csAccount);
            reply.setCsAcc(csAcc);
        }
        return reply;
    }
}
