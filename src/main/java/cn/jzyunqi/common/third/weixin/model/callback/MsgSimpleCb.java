package cn.jzyunqi.common.third.weixin.model.callback;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.lang.annotation.Target;

/**
 * @author wiiyaya
 * @since 2021/5/9.
 */
@Getter
@Setter
@ToString
public class MsgSimpleCb implements Serializable {
    @Serial
    private static final long serialVersionUID = 6125983649333864559L;

    private String signature;

    private String timestamp;

    private String nonce;

    private String echostr;

    private String msg_signature;

}
