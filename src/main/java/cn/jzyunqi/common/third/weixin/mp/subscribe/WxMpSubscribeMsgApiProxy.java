package cn.jzyunqi.common.third.weixin.mp.subscribe;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.third.weixin.common.WxHttpExchange;
import cn.jzyunqi.common.third.weixin.common.model.WeixinRspV1;
import cn.jzyunqi.common.third.weixin.common.model.WeixinRspV2;
import cn.jzyunqi.common.third.weixin.mp.subscribe.model.WxMpPubTemplateKeywordData;
import cn.jzyunqi.common.third.weixin.mp.subscribe.model.WxMpCategoryRsp;
import cn.jzyunqi.common.third.weixin.mp.subscribe.model.WxMpMsgTemplateData;
import cn.jzyunqi.common.third.weixin.mp.subscribe.model.WxMpMsgTemplateParam;
import cn.jzyunqi.common.third.weixin.mp.subscribe.model.WxMpPubTemplateTitleData;
import cn.jzyunqi.common.third.weixin.mp.subscribe.model.WxMpPubTemplateTitleParam;
import cn.jzyunqi.common.third.weixin.mp.template.model.WxMpTemplateMsgParam;
import cn.jzyunqi.common.third.weixin.mp.template.model.WxMpTemplateMsgRsp;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/**
 * @author wiiyaya
 * @since 2024/9/23
 */
@WxHttpExchange
@HttpExchange(url = "https://api.weixin.qq.com", accept = {"application/json"})
public interface WxMpSubscribeMsgApiProxy {

    //订阅通知 - 从公共模板库中选用模板，到私有模板库中
    @PostExchange(url = "/wxaapi/newtmpl/addtemplate")
    WxMpMsgTemplateData addTemplate(@RequestParam String access_token, @RequestBody WxMpMsgTemplateParam request) throws BusinessException;

    //订阅通知 - 删除私有模板库中的模板
    @PostExchange(url = "/wxaapi/newtmpl/deltemplate")
    WeixinRspV1 delTemplate(@RequestParam String access_token, @RequestBody WxMpMsgTemplateData request) throws BusinessException;

    //订阅通知 - 获取公众号类目
    @PostExchange(url = "/wxaapi/newtmpl/getcategory")
    WxMpCategoryRsp getCategory(@RequestParam String access_token) throws BusinessException;

    //订阅通知 - 获取公共模板下的关键词列表
    @PostExchange(url = "/wxaapi/newtmpl/getpubtemplatekeywords")
    WeixinRspV2<WxMpPubTemplateKeywordData> getPubTemplateKeyWordsById(@RequestParam String access_token, @RequestBody WxMpMsgTemplateParam request) throws BusinessException;

    //订阅通知 - 获取类目下的公共模板
    @PostExchange(url = "/wxaapi/newtmpl/getpubtemplatetitles")
    WeixinRspV2<WxMpPubTemplateTitleData> getPubTemplateTitleList(@RequestParam String access_token, @RequestBody WxMpPubTemplateTitleParam request) throws BusinessException;

    //订阅通知 - 获取私有的模板列表
    @PostExchange(url = "/wxaapi/newtmpl/gettemplate")
    WeixinRspV2<WxMpMsgTemplateData> getTemplateList(@RequestParam String access_token) throws BusinessException;

    //订阅通知 - 发送订阅通知
    @PostExchange(url = "/cgi-bin/message/subscribe/bizsend")
    WxMpTemplateMsgRsp send(@RequestParam String access_token, WxMpTemplateMsgParam request) throws BusinessException;
}
