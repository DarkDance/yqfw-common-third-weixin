package cn.jzyunqi.common.third.weixin.model.response;

import cn.jzyunqi.common.third.weixin.model.response.item.PlantCertData;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author wiiyaya
 * @date 2021/7/11.
 */
@Getter
@Setter
public class PlantCertRsp implements Serializable {
    @Serial
    private static final long serialVersionUID = 689850960211225825L;

    private String code;

    private String message;

    private List<PlantCertData> data;
}
