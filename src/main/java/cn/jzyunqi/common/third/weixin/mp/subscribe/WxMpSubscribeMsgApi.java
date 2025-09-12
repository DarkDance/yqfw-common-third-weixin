package cn.jzyunqi.common.third.weixin.mp.subscribe;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.third.weixin.common.model.WeixinRspV2;
import cn.jzyunqi.common.third.weixin.mp.subscribe.model.WxMpCategoryData;
import cn.jzyunqi.common.third.weixin.mp.subscribe.model.WxMpPubTemplateKeywordData;
import cn.jzyunqi.common.third.weixin.mp.subscribe.model.WxMpPubTemplateTitleData;
import cn.jzyunqi.common.third.weixin.mp.subscribe.model.WxMpPubTemplateTitleParam;
import cn.jzyunqi.common.third.weixin.mp.subscribe.model.WxMpTemplateData;
import cn.jzyunqi.common.third.weixin.mp.subscribe.model.WxMpTemplateParam;
import cn.jzyunqi.common.third.weixin.mp.template.model.WxMpTemplateMsgParam;
import cn.jzyunqi.common.third.weixin.mp.token.WxMpTokenApi;
import jakarta.annotation.Resource;

import java.util.List;

/**
 * @author wiiyaya
 * @since 2025/9/12
 */
public class WxMpSubscribeMsgApi extends WxMpTokenApi {

    @Resource
    private WxMpSubscribeMsgApiProxy wxMpSubscribeMsgApiProxy;

    //订阅通知 - 从公共模板库中选用模板，到私有模板库中
    public String privateTemplateAdd(String wxMpAppId, String publicTemplateId, List<Integer> keywordIdList, String sceneDesc) throws BusinessException {
        WxMpTemplateParam request = new WxMpTemplateParam();
        request.setTid(publicTemplateId);
        request.setKidList(keywordIdList);
        request.setSceneDesc(sceneDesc);
        return wxMpSubscribeMsgApiProxy.privateTemplateAdd(getClientToken(wxMpAppId), request).getPriTmplId();
    }

    //订阅通知 - 删除私有模板库中的模板
    public void privateTemplateDelete(String wxMpAppId, String privateTemplateId) throws BusinessException {
        WxMpTemplateData request = new WxMpTemplateData();
        request.setPriTmplId(privateTemplateId);
        wxMpSubscribeMsgApiProxy.privateTemplateDelete(getClientToken(wxMpAppId), request);
    }

    //订阅通知 - 获取公众号类目
    public List<WxMpCategoryData> categoryList(String wxMpAppId) throws BusinessException {
        return wxMpSubscribeMsgApiProxy.categoryList(getClientToken(wxMpAppId)).getData();
    }

    //订阅通知 - 获取公共模板下的关键词列表
    public List<WxMpPubTemplateKeywordData> publicTemplateKeywordsList(String wxMpAppId, String publicTemplateId) throws BusinessException {
        WxMpTemplateParam request = new WxMpTemplateParam();
        request.setTid(publicTemplateId);
        return wxMpSubscribeMsgApiProxy.publicTemplateKeywordsList(getClientToken(wxMpAppId), request).getData();
    }

    //订阅通知 - 获取类目下的公共模板
    public WeixinRspV2<List<WxMpPubTemplateTitleData>> publicTemplatePage(String wxMpAppId, String categoryIds, int start, int limit) throws BusinessException {
        WxMpPubTemplateTitleParam request = new WxMpPubTemplateTitleParam();
        request.setIds(categoryIds);
        request.setStart(start);
        request.setLimit(limit);
        return wxMpSubscribeMsgApiProxy.publicTemplatePage(getClientToken(wxMpAppId), request);
    }

    //订阅通知 - 获取私有的模板列表
    public List<WxMpTemplateData> privateTemplateList(String wxMpAppId) throws BusinessException {
        return wxMpSubscribeMsgApiProxy.privateTemplateList(getClientToken(wxMpAppId)).getData();
    }

    //订阅通知 - 发送订阅通知
    public void sendSubscribeMsg(String wxMpAppId, WxMpTemplateMsgParam request) throws BusinessException {
        wxMpSubscribeMsgApiProxy.sendSubscribeMsg(getClientToken(wxMpAppId), request);
    }
}
