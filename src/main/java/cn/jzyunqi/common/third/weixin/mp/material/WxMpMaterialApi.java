package cn.jzyunqi.common.third.weixin.mp.material;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.third.weixin.common.model.WeixinRspV1;
import cn.jzyunqi.common.third.weixin.mp.material.enums.MaterialType;
import cn.jzyunqi.common.third.weixin.mp.material.model.WxMpMaterialCountData;
import cn.jzyunqi.common.third.weixin.mp.material.model.WxMpMaterialNewsData;
import cn.jzyunqi.common.third.weixin.mp.material.model.WxMpMaterialSearchParam;
import cn.jzyunqi.common.third.weixin.mp.material.model.WxMpMaterialSearchRsp;
import cn.jzyunqi.common.third.weixin.mp.material.model.WxMpMaterialVideoParam;
import cn.jzyunqi.common.third.weixin.mp.material.model.WxMpMaterialVideoRsp;
import cn.jzyunqi.common.third.weixin.mp.material.model.WxMpMediaData;
import cn.jzyunqi.common.third.weixin.mp.token.WxMpTokenApi;
import jakarta.annotation.Resource;

/**
 * @author wiiyaya
 * @since 2025/9/12
 */
public class WxMpMaterialApi {

    @Resource
    private WxMpTokenApi wxMpTokenApi;

    @Resource
    private WxMpMaterialApiProxy wxMpMaterialApiProxy;

    //素材管理 - 新增临时素材
    public WxMpMediaData tempMaterialUpload(String wxMpAppId, MaterialType type, org.springframework.core.io.Resource media) throws BusinessException {
        return wxMpMaterialApiProxy.mediaUpload(wxMpTokenApi.getClientToken(wxMpAppId), type, media);
    }

    //素材管理 - 获取临时素材
    public org.springframework.core.io.Resource tempMaterialDownload(String wxMpAppId, String mediaId) throws BusinessException {
        return wxMpMaterialApiProxy.mediaDownload(wxMpTokenApi.getClientToken(wxMpAppId), mediaId);
    }

    //素材管理 - 获取临时高清语音素材
    public org.springframework.core.io.Resource tempVoiceDownload(String wxMpAppId, String mediaId) throws BusinessException {
        return wxMpMaterialApiProxy.jssdkMediaDownload(wxMpTokenApi.getClientToken(wxMpAppId), mediaId);
    }

    //素材管理 - 新增图片永久素材
    public WxMpMediaData imageUpload(String wxMpAppId, org.springframework.core.io.Resource media) throws BusinessException {
        return wxMpMaterialApiProxy.mediaImgUpload(wxMpTokenApi.getClientToken(wxMpAppId), media);
    }

    //素材管理 - 新增其它永久素材
    public WxMpMediaData materialUpload(String wxMpAppId, MaterialType type, WxMpMaterialVideoParam videoParam, org.springframework.core.io.Resource media) throws BusinessException {
        if (type != MaterialType.video) {
            videoParam = null;
        }
        return wxMpMaterialApiProxy.materialUpload(wxMpTokenApi.getClientToken(wxMpAppId), type, videoParam, media);
    }

    //素材管理 - 图文永久素材获取
    public WxMpMaterialNewsData newsInfo(String wxMpAppId, String mediaId) throws BusinessException {
        WxMpMediaData request = new WxMpMediaData();
        request.setMediaId(mediaId);
        return wxMpMaterialApiProxy.materialNewsInfo(wxMpTokenApi.getClientToken(wxMpAppId), request);
    }

    //素材管理 - 视频永久素材获取
    public WxMpMaterialVideoRsp videoInfo(String wxMpAppId, String mediaId) throws BusinessException {
        WxMpMediaData request = new WxMpMediaData();
        request.setMediaId(mediaId);
        return wxMpMaterialApiProxy.materialVideoInfo(wxMpTokenApi.getClientToken(wxMpAppId), request);
    }

    //素材管理 - 其它永久素材获取
    public org.springframework.core.io.Resource materialDownload(String wxMpAppId, String mediaId) throws BusinessException {
        WxMpMediaData request = new WxMpMediaData();
        request.setMediaId(mediaId);
        return wxMpMaterialApiProxy.materialOtherDownload(wxMpTokenApi.getClientToken(wxMpAppId), request);
    }

    //素材管理 - 删除永久素材
    public WeixinRspV1 materialDelete(String wxMpAppId, String mediaId) throws BusinessException {
        WxMpMediaData request = new WxMpMediaData();
        request.setMediaId(mediaId);
        return wxMpMaterialApiProxy.materialDelete(wxMpTokenApi.getClientToken(wxMpAppId), request);
    }

    //素材管理 - 获取永久素材总数
    public WxMpMaterialCountData materialCount(String wxMpAppId) throws BusinessException {
        return wxMpMaterialApiProxy.materialCount(wxMpTokenApi.getClientToken(wxMpAppId));
    }

    //素材管理 - 获取永久素材列表
    public WxMpMaterialSearchRsp materialPage(String wxMpAppId, MaterialType type, Integer offset, Integer count) throws BusinessException {
        WxMpMaterialSearchParam request = new WxMpMaterialSearchParam();
        request.setType(type);
        request.setOffset(offset);
        request.setCount(count);
        return wxMpMaterialApiProxy.materialBatchGet(wxMpTokenApi.getClientToken(wxMpAppId), request);
    }
}
