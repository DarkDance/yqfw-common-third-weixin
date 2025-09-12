package cn.jzyunqi.common.third.weixin.mp.kefu;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.third.weixin.common.model.WeixinRspV1;
import cn.jzyunqi.common.third.weixin.mp.callback.model.ReplyMsgData;
import cn.jzyunqi.common.third.weixin.mp.kefu.enums.TypingType;
import cn.jzyunqi.common.third.weixin.mp.kefu.model.WxMpKfAccountParam;
import cn.jzyunqi.common.third.weixin.mp.kefu.model.WxMpKfListRsp;
import cn.jzyunqi.common.third.weixin.mp.kefu.model.WxMpKfMsgListParam;
import cn.jzyunqi.common.third.weixin.mp.kefu.model.WxMpKfMsgListRsp;
import cn.jzyunqi.common.third.weixin.mp.kefu.model.WxMpKfSessionData;
import cn.jzyunqi.common.third.weixin.mp.kefu.model.WxMpKfSessionListRsp;
import cn.jzyunqi.common.third.weixin.mp.kefu.model.WxMpKfTypingParam;
import cn.jzyunqi.common.third.weixin.mp.token.WxMpTokenApi;
import cn.jzyunqi.common.utils.DateTimeUtilPlus;
import jakarta.annotation.Resource;

import java.time.LocalDateTime;

/**
 * @author wiiyaya
 * @since 2025/9/12
 */
public class WxMpKefuApi {

    @Resource
    private WxMpTokenApi wxMpTokenApi;

    @Resource
    private WxMpKfApiProxy wxMpKfApiProxy;

    //客服管理 - 添加客服账号（添加后不可用，需要再邀请）
    public WeixinRspV1 kfAccountAdd(String wxMpAppId, WxMpKfAccountParam request) throws BusinessException {
        return wxMpKfApiProxy.kfAccountAdd(wxMpTokenApi.getClientToken(wxMpAppId), request);
    }

    //客服管理 - 邀请绑定客服账号
    public WeixinRspV1 kfAccountInviteWorker(String wxMpAppId, WxMpKfAccountParam request) throws BusinessException {
        return wxMpKfApiProxy.kfAccountInviteWorker(wxMpTokenApi.getClientToken(wxMpAppId), request);
    }

    //客服管理 - 修改客服账号
    public WeixinRspV1 kfAccountUpdate(String wxMpAppId, WxMpKfAccountParam request) throws BusinessException {
        return wxMpKfApiProxy.kfAccountUpdate(wxMpTokenApi.getClientToken(wxMpAppId), request);
    }

    //客服管理 - 删除客服账号
    public WeixinRspV1 kfAccountDel(String wxMpAppId, String kfAccount) throws BusinessException {
        WxMpKfAccountParam request = new WxMpKfAccountParam();
        request.setKfAccount(kfAccount);
        return wxMpKfApiProxy.kfAccountDel(wxMpTokenApi.getClientToken(wxMpAppId), request);
    }

    //客服管理 - 设置客服账号的头像，文件大小为5M以内
    public WeixinRspV1 kfAccountUploadHeadImg(String wxMpAppId, String kfAccount, org.springframework.core.io.Resource media) throws BusinessException {
        return wxMpKfApiProxy.kfAccountUploadHeadImg(wxMpTokenApi.getClientToken(wxMpAppId), kfAccount, media);
    }

    //客服管理 - 获取所有客服账号
    public WxMpKfListRsp kfList(String wxMpAppId) throws BusinessException {
        return wxMpKfApiProxy.kfList(wxMpTokenApi.getClientToken(wxMpAppId));
    }

    //客服管理 - 获取所有客服在线账号
    public WxMpKfListRsp kfOnlineList(String wxMpAppId) throws BusinessException {
        return wxMpKfApiProxy.kfOnlineList(wxMpTokenApi.getClientToken(wxMpAppId));
    }

    //客服消息 - 发消息
    public void sendKefuMessageWithResponse(String wxMpAppId, ReplyMsgData request) throws BusinessException {
        wxMpKfApiProxy.sendKefuMessageWithResponse(wxMpTokenApi.getClientToken(wxMpAppId), request);
    }

    //客服消息 - 发送客服输入状态
    public void sendKfTypingState(String wxMpAppId, String openid, TypingType command) throws BusinessException {
        WxMpKfTypingParam request = new WxMpKfTypingParam();
        request.setToUser(openid);
        request.setCommand(command);
        wxMpKfApiProxy.sendKfTypingState(wxMpTokenApi.getClientToken(wxMpAppId), request);
    }

    //会话控制 - 创建会话
    public void kfSessionCreate(String wxMpAppId, String openid, String kfAccount) throws BusinessException {
        WxMpKfSessionData request = new WxMpKfSessionData();
        request.setOpenId(openid);
        request.setKfAccount(kfAccount);
        wxMpKfApiProxy.kfSessionCreate(wxMpTokenApi.getClientToken(wxMpAppId), request);
    }

    //会话控制 - 关闭会话
    public void kfSessionClose(String wxMpAppId, String openid, String kfAccount) throws BusinessException {
        WxMpKfSessionData request = new WxMpKfSessionData();
        request.setOpenId(openid);
        request.setKfAccount(kfAccount);
        wxMpKfApiProxy.kfSessionClose(wxMpTokenApi.getClientToken(wxMpAppId), request);
    }

    //会话控制 - 获取客户会话状态
    public WxMpKfSessionData kfSessionInfo(String wxMpAppId, String openid) throws BusinessException {
        return wxMpKfApiProxy.kfSessionInfo(wxMpTokenApi.getClientToken(wxMpAppId), openid);
    }

    //会话控制 - 获取客服会话列表
    public WxMpKfSessionListRsp kfSessionList(String wxMpAppId, String kfAccount) throws BusinessException {
        return wxMpKfApiProxy.kfSessionList(wxMpTokenApi.getClientToken(wxMpAppId), kfAccount);
    }

    //会话控制 - 获取未接入会话列表
    public WxMpKfSessionListRsp kfSessionWaitCase(String wxMpAppId) throws BusinessException {
        return wxMpKfApiProxy.kfSessionWaitCase(wxMpTokenApi.getClientToken(wxMpAppId));
    }

    //获取聊天记录
    public WxMpKfMsgListRsp kfMsgList(String wxMpAppId, LocalDateTime startTime, LocalDateTime endTime, Long msgId, Integer number) throws BusinessException {
        WxMpKfMsgListParam request = new WxMpKfMsgListParam();
        request.setStartTime(DateTimeUtilPlus.toEpochSecond(startTime));
        request.setEndTime(DateTimeUtilPlus.toEpochSecond(endTime));
        request.setMsgId(msgId);
        request.setNumber(number);
        return wxMpKfApiProxy.kfMsgList(wxMpTokenApi.getClientToken(wxMpAppId), request);
    }
}
