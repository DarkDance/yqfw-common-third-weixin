package cn.jzyunqi.common.third.weixin.mp.kefu.model;

import cn.jzyunqi.common.third.weixin.common.model.WeixinRspV1;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author wiiyaya
 * @since 2024/9/23
 */
@Getter
@Setter
public class WxMpKfSessionListRsp extends WeixinRspV1 {

    /**
     * 会话列表
     */
    @JsonProperty("sessionlist")
    private List<WxMpKfSessionData> kfSessionList;

    /**
     * 未接入会话数量
     */
    private Long count;

    /**
     * 未接入会话列表，最多返回100条数据
     */
    @JsonProperty("waitcaselist")
    private List<WxMpKfSessionData> waitCaseList;
}
