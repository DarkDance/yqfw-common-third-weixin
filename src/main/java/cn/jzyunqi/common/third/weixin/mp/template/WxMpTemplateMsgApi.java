package cn.jzyunqi.common.third.weixin.mp.template;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.third.weixin.mp.template.enums.WxMpIndustryEnum;
import cn.jzyunqi.common.third.weixin.mp.template.model.WxMpAddTemplateParam;
import cn.jzyunqi.common.third.weixin.mp.template.model.WxMpIndustryData;
import cn.jzyunqi.common.third.weixin.mp.template.model.WxMpIndustryParam;
import cn.jzyunqi.common.third.weixin.mp.template.model.WxMpTemplateMsgParam;
import cn.jzyunqi.common.third.weixin.mp.token.WxMpTokenApi;
import jakarta.annotation.Resource;

import java.util.List;

/**
 * @author wiiyaya
 * @since 2025/9/12
 */
public class WxMpTemplateMsgApi {

    @Resource
    private WxMpTokenApi wxMpTokenApi;

    @Resource
    private WxMpTemplateMsgApiProxy wxMpTemplateMsgApiProxy;

    //基础消息 - 模板消息 - 设置所属行业
    public void industrySet(String wxMpAppId, WxMpIndustryEnum primaryIndustry, WxMpIndustryEnum secondaryIndustry) throws BusinessException {
        WxMpIndustryParam request = new WxMpIndustryParam();
        request.setIndustryId1(primaryIndustry.getCode());
        request.setIndustryId2(secondaryIndustry.getCode());
        wxMpTemplateMsgApiProxy.industrySet(wxMpTokenApi.getClientToken(wxMpAppId), request);
    }

    //基础消息 - 模板消息 - 获取设置的行业信息
    public WxMpIndustryData industryInfo(String wxMpAppId) throws BusinessException {
        return wxMpTemplateMsgApiProxy.industryInfo(wxMpTokenApi.getClientToken(wxMpAppId));
    }

    //基础消息 - 模板消息 - 获得模板ID
    public String privateTemplateAdd(String wxMpAppId, String shortTemplateId, List<String> keywordNameList) throws BusinessException {
        WxMpAddTemplateParam request = new WxMpAddTemplateParam();
        request.setTemplateIdShort(shortTemplateId);
        request.setKeywordNameList(keywordNameList);
        return wxMpTemplateMsgApiProxy.privateTemplateAdd(wxMpTokenApi.getClientToken(wxMpAppId), request).getTemplateId();
    }

    //基础消息 - 模板消息 - 获取模板列表
    public List<cn.jzyunqi.common.third.weixin.mp.template.model.WxMpTemplateData> privateTemplateList(String wxMpAppId) throws BusinessException {
        return wxMpTemplateMsgApiProxy.privateTemplateList(wxMpTokenApi.getClientToken(wxMpAppId)).getTemplateList();
    }

    //基础消息 - 模板消息 - 获取模板列表
    public void privateTemplateDelete(String wxMpAppId, String templateId) throws BusinessException {
        cn.jzyunqi.common.third.weixin.mp.template.model.WxMpTemplateData request = new cn.jzyunqi.common.third.weixin.mp.template.model.WxMpTemplateData();
        request.setTemplateId(templateId);
        wxMpTemplateMsgApiProxy.privateTemplateDelete(wxMpTokenApi.getClientToken(wxMpAppId), request);
    }

    //基础消息 - 模板消息 - 发送模板消息
    public String sendTemplateMsg(String wxMpAppId, WxMpTemplateMsgParam request) throws BusinessException {
        return wxMpTemplateMsgApiProxy.sendTemplateMsg(wxMpTokenApi.getClientToken(wxMpAppId), request).getMsgId();
    }
}
