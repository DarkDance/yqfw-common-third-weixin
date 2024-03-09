package cn.jzyunqi.common.third.weixin.model.callback;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author wiiyaya
 * @date 2021/5/9.
 */
@Getter
@Setter
public class MsgSimpleCb implements Serializable {
    private static final long serialVersionUID = 6125983649333864559L;

    private String signature;

    private String timestamp;

    private String nonce;

    private String echostr;

    private String msg_signature;

}
