package cn.jzyunqi.common.third.weixin.mp.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author wiiyaya
 * @since 2018/9/6.
 */
@Getter
@Setter
@ToString
public class PhoneData implements Serializable {
    @Serial
    private static final long serialVersionUID = -2495073819623587868L;

    /**
     * 用户绑定的手机号（国外手机号会有区号）
     */
    private String phoneNumber;

    /**
     * 没有区号的手机号
     */
    private String purePhoneNumber;

    /**
     * 区号
     */
    private String countryCode;

}
