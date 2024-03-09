package cn.jzyunqi.common.third.weixin.model.response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author wiiyaya
 * @date 2021/7/20.
 */
@Getter
@Setter
public class UnifiedOrderV3Rsp implements Serializable {
    @Serial
    private static final long serialVersionUID = 3444826075123710160L;

    private String prepayId;
}
