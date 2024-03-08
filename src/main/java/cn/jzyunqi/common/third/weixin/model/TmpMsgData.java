package cn.jzyunqi.common.third.weixin.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author wiiyaya
 * @date 2021/5/7.
 */
@Getter
@Setter
@AllArgsConstructor
public class TmpMsgData implements Serializable {
    private static final long serialVersionUID = 6716671744535197873L;

    private String key;

    private String value;
}
