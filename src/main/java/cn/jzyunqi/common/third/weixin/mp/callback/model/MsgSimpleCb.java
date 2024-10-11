package cn.jzyunqi.common.third.weixin.mp.callback.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author wiiyaya
 * @since 2021/5/9.
 */
@Getter
@Setter
@ToString
public class MsgSimpleCb{

    private String signature;

    private String timestamp;

    private String nonce;

    private String echostr;

    private String msg_signature;

}
