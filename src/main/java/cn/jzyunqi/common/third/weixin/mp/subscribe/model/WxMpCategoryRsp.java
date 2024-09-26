package cn.jzyunqi.common.third.weixin.mp.subscribe.model;

import cn.jzyunqi.common.third.weixin.common.model.WeixinRspV1;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author wiiyaya
 * @since 2024/9/23
 */
@Getter
@Setter
public class WxMpCategoryRsp extends WeixinRspV1 {

    private List<WxMpCategoryData> data;
}
