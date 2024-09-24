package cn.jzyunqi.common.third.weixin.mp.kefu;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.third.weixin.common.WxHttpExchange;
import cn.jzyunqi.common.third.weixin.common.model.WeixinRsp;
import cn.jzyunqi.common.third.weixin.mp.kefu.model.WxMpKfAccountParam;
import cn.jzyunqi.common.third.weixin.mp.kefu.model.WxMpKfListRsp;
import cn.jzyunqi.common.third.weixin.mp.kefu.model.WxMpKfMsgListParam;
import cn.jzyunqi.common.third.weixin.mp.kefu.model.WxMpKfMsgListRsp;
import cn.jzyunqi.common.third.weixin.mp.kefu.model.WxMpKfSessionData;
import cn.jzyunqi.common.third.weixin.mp.kefu.model.WxMpKfSessionListRsp;
import cn.jzyunqi.common.third.weixin.mp.kefu.model.WxMpKfTypingParam;
import cn.jzyunqi.common.third.weixin.mp.callback.model.ReplyMsgData;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/**
 * 客服消息API
 *
 * @author wiiyaya
 * @since 2024/9/20
 */
@WxHttpExchange
@HttpExchange(url = "https://api.weixin.qq.com", accept = {"application/json"})
public interface WxMpKfApiProxy {

    //客服管理 - 添加客服账号（添加后不可用，需要再邀请）
    @PostExchange(url = "/customservice/kfaccount/add")
    WeixinRsp kfAccountAdd(@RequestParam String access_token, @RequestBody WxMpKfAccountParam request) throws BusinessException;

    //客服管理 - 邀请绑定客服账号
    @PostExchange(url = "/customservice/kfaccount/inviteworker")
    WeixinRsp kfAccountInviteWorker(@RequestParam String access_token, @RequestBody WxMpKfAccountParam request) throws BusinessException;

    //客服管理 - 修改客服账号
    @PostExchange(url = "/customservice/kfaccount/update")
    WeixinRsp kfAccountUpdate(@RequestParam String access_token, @RequestBody WxMpKfAccountParam request) throws BusinessException;

    //客服管理 - 删除客服账号
    @PostExchange(url = "/customservice/kfaccount/del")
    WeixinRsp kfAccountDel(@RequestParam String access_token, @RequestBody WxMpKfAccountParam request) throws BusinessException;

    //客服管理 - 设置客服账号的头像，文件大小为5M以内
    @PostExchange(url = "/customservice/kfaccount/uploadheadimg", contentType = "multipart/form-data")
    WeixinRsp kfAccountUploadHeadImg(@RequestParam String access_token, @RequestParam String kf_account, @RequestParam Resource media) throws BusinessException;

    //客服管理 - 获取所有客服账号
    @PostExchange(url = "/cgi-bin/customservice/getkflist")
    WxMpKfListRsp kfList(@RequestParam String access_token) throws BusinessException;

    //客服管理 - 获取所有客服在线账号
    @PostExchange(url = "/cgi-bin/customservice/getonlinekflist")
    WxMpKfListRsp kfOnlineList(@RequestParam String access_token) throws BusinessException;

    //客服消息 - 发消息
    @PostExchange(url = "/cgi-bin/message/custom/send")
    String sendKefuMessageWithResponse(@RequestParam String access_token, @RequestBody ReplyMsgData request) throws BusinessException;

    //客服消息 - 发送客服输入状态
    @PostExchange(url = "/cgi-bin/message/custom/typing")
    String sendKfTypingState(@RequestParam String access_token, @RequestBody WxMpKfTypingParam request) throws BusinessException;

    //会话控制 - 创建会话
    @PostExchange(url = "/customservice/kfsession/create")
    String kfSessionCreate(@RequestParam String access_token, @RequestBody WxMpKfSessionData request) throws BusinessException;

    //会话控制 - 关闭会话
    @PostExchange(url = "/customservice/kfsession/close")
    String kfSessionClose(@RequestParam String access_token, @RequestBody WxMpKfSessionData request) throws BusinessException;

    //会话控制 - 获取客户会话状态
    @GetExchange(url = "/customservice/kfsession/getsession")
    WxMpKfSessionData kfSessionInfo(@RequestParam String access_token, @RequestParam String openid) throws BusinessException;

    //会话控制 - 获取客服会话列表
    @GetExchange(url = "/customservice/kfsession/getsessionlist")
    WxMpKfSessionListRsp kfSessionList(@RequestParam String access_token, @RequestParam String kf_account) throws BusinessException;

    //会话控制 - 获取未接入会话列表
    @GetExchange(url = "/customservice/kfsession/getwaitcase")
    WxMpKfSessionListRsp kfSessionWaitCase(@RequestParam String access_token) throws BusinessException;

    //获取聊天记录
    @PostExchange(url = "/customservice/msgrecord/getmsglist")
    WxMpKfMsgListRsp kfMsgList(@RequestParam String access_token, @RequestBody WxMpKfMsgListParam request) throws BusinessException;

}
