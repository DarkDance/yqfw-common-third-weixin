package cn.jzyunqi.common.third.weixin.cp.token.model;

import cn.jzyunqi.common.third.weixin.common.model.WeixinRspV1;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author wiiyaya
 * @since 2025/11/17
 */
@Getter
@Setter
@ToString
public class IpListData extends WeixinRspV1 {

    private List<String> ip_list;
}
