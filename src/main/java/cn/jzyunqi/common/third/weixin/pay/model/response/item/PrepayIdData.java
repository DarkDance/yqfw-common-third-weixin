package cn.jzyunqi.common.third.weixin.pay.model.response.item;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author wiiyaya
 * @since 2021/7/20.
 */
@Getter
@Setter
public class PrepayIdData implements Serializable {
    @Serial
    private static final long serialVersionUID = 3444826075123710160L;

    private String prepayId;
}
