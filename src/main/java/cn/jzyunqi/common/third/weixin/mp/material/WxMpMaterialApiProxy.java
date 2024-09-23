package cn.jzyunqi.common.third.weixin.mp.material;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.third.weixin.common.WxHttpExchange;
import cn.jzyunqi.common.third.weixin.common.model.WeixinRsp;
import cn.jzyunqi.common.third.weixin.mp.material.enums.MaterialType;
import cn.jzyunqi.common.third.weixin.mp.material.model.WxMpMaterialCountData;
import cn.jzyunqi.common.third.weixin.mp.material.model.WxMpMaterialSearchParam;
import cn.jzyunqi.common.third.weixin.mp.material.model.WxMpMaterialSearchRsp;
import cn.jzyunqi.common.third.weixin.mp.material.model.WxMpMediaData;
import cn.jzyunqi.common.third.weixin.mp.material.model.WxMpMaterialNewsData;
import cn.jzyunqi.common.third.weixin.mp.material.model.WxMpMaterialVideoRsp;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.io.File;
import java.io.InputStream;

/**
 * @author wiiyaya
 * @since 2024/9/23
 */
@WxHttpExchange
@HttpExchange(url = "https://api.weixin.qq.com", accept = {"application/json"})
public interface WxMpMaterialApiProxy {

    //素材管理 - 新增临时素材
    @PostExchange(url = "/cgi-bin/media/upload", contentType = "multipart/form-data")
    WxMpMediaData mediaUpload(@RequestParam String access_token, @RequestParam MaterialType type, @RequestParam MultipartFile media) throws BusinessException;

    //素材管理 - 获取临时素材
    @PostExchange(url = "/cgi-bin/media/get")
    File mediaDownload(@RequestParam String access_token, @RequestParam String media_id) throws BusinessException;

    //素材管理 - 获取高清语音素材
    @PostExchange(url = "/cgi-bin/media/get/jssdk")
    File jssdkMediaDownload(@RequestParam String access_token, @RequestParam String media_id) throws BusinessException;

    //素材管理 - 新增永久图片素材
    @PostExchange(url = "/cgi-bin/media/uploadimg", contentType = "multipart/form-data")
    WxMpMediaData mediaImgUpload(@RequestParam String access_token, @RequestParam MultipartFile media) throws BusinessException;

    //素材管理 - 新增永久素材
    @PostExchange(url = "/cgi-bin/material/add_material", contentType = "multipart/form-data")
    WxMpMediaData materialOtherUpload(@RequestParam String access_token, @RequestParam MaterialType type, @RequestParam MultipartFile media) throws BusinessException;

    //素材管理 - 图文永久素材获取
    @PostExchange(url = "/cgi-bin/material/get_material")
    WxMpMaterialNewsData materialNewsInfo(@RequestParam String access_token, @RequestBody WxMpMediaData request) throws BusinessException;

    //素材管理 - 视频永久素材获取
    @PostExchange(url = "/cgi-bin/material/get_material")
    WxMpMaterialVideoRsp materialVideoInfo(@RequestParam String access_token, @RequestBody WxMpMediaData request) throws BusinessException;

    //素材管理 - 其它永久素材获取
    @PostExchange(url = "/cgi-bin/material/get_material")
    InputStream materialOtherDownload(@RequestParam String access_token, @RequestBody WxMpMediaData request) throws BusinessException;

    //素材管理 - 删除永久素材
    @PostExchange(url = "/cgi-bin/material/del_material")
    WeixinRsp materialDelete(@RequestParam String access_token, @RequestBody WxMpMediaData request) throws BusinessException;

    //素材管理 - 获取永久素材总数
    @PostExchange(url = "/cgi-bin/material/get_materialcount")
    WxMpMaterialCountData materialCount(@RequestParam String access_token) throws BusinessException;

    //素材管理 - 获取永久素材列表
    @PostExchange(url = "/cgi-bin/material/batchget_material")
    WxMpMaterialSearchRsp materialBatchGet(@RequestParam String access_token, @RequestBody WxMpMaterialSearchParam request) throws BusinessException;
}
