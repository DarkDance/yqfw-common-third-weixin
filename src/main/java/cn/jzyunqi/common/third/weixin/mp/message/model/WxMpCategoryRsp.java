package cn.jzyunqi.common.third.weixin.mp.message.model;

import cn.jzyunqi.common.third.weixin.common.model.WeixinRsp;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author wiiyaya
 * @since 2024/9/23
 */
@Getter
@Setter
public class WxMpCategoryRsp extends WeixinRsp {

    private List<WxMpCategoryData> data;
}
