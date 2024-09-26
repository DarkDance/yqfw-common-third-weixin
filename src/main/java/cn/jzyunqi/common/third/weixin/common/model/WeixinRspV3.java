package cn.jzyunqi.common.third.weixin.common.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wiiyaya
 * @since 2018/5/22.
 */
@Getter
@Setter
public class WeixinRspV3<T>  {

    private String code;

    private String message;

    private T data;

}
