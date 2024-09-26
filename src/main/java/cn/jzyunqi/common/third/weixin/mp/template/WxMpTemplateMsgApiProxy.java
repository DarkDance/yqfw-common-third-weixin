package cn.jzyunqi.common.third.weixin.mp.template;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.third.weixin.common.WxHttpExchange;
import cn.jzyunqi.common.third.weixin.common.model.WeixinRspV1;
import cn.jzyunqi.common.third.weixin.mp.template.model.WxMpAddTemplateParam;
import cn.jzyunqi.common.third.weixin.mp.template.model.WxMpAllTemplateRsp;
import cn.jzyunqi.common.third.weixin.mp.template.model.WxMpTemplateData;
import cn.jzyunqi.common.third.weixin.mp.template.model.WxMpIndustryParam;
import cn.jzyunqi.common.third.weixin.mp.template.model.WxMpIndustryData;
import cn.jzyunqi.common.third.weixin.mp.template.model.WxMpTemplateMsgParam;
import cn.jzyunqi.common.third.weixin.mp.template.model.WxMpTemplateMsgRsp;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/**
 * @author wiiyaya
 * @since 2024/9/26
 */
@WxHttpExchange
@HttpExchange(url = "https://api.weixin.qq.com", accept = {"application/json"})
public interface WxMpTemplateMsgApiProxy {

    //基础消息 - 模板消息 - 设置所属行业
    @PostExchange(url = "/cgi-bin/template/api_set_industry")
    WeixinRspV1 industrySet(@RequestParam String access_token, @RequestBody WxMpIndustryParam request) throws BusinessException;

    //基础消息 - 模板消息 - 获取设置的行业信息
    @GetExchange(url = "/cgi-bin/template/api_set_industry")
    WxMpIndustryData industryInfo(@RequestParam String access_token) throws BusinessException;

    //基础消息 - 模板消息 - 获得模板ID
    @PostExchange(url = "/cgi-bin/template/api_add_template")
    WxMpTemplateData privateTemplateAdd(@RequestParam String access_token, @RequestBody WxMpAddTemplateParam request) throws BusinessException;

    //基础消息 - 模板消息 - 获取模板列表
    @GetExchange(url = "/cgi-bin/template/get_all_private_template")
    WxMpAllTemplateRsp privateTemplateList(@RequestParam String access_token) throws BusinessException;

    //基础消息 - 模板消息 - 获取模板列表
    @PostExchange(url = "/cgi-bin/template/del_private_template")
    WeixinRspV1 privateTemplateDelete(@RequestParam String access_token, @RequestBody WxMpTemplateData request) throws BusinessException;

    //基础消息 - 模板消息 - 发送模板消息
    @PostExchange(url = "/cgi-bin/message/template/send")
    WxMpTemplateMsgRsp sendTemplateMsg(@RequestParam String access_token, @RequestBody WxMpTemplateMsgParam request) throws BusinessException;
}
