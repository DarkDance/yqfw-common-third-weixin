package cn.jzyunqi.common.third.weixin.model.request.item;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author wiiyaya
 * @date 2021/6/4.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LineColorData implements Serializable {
    @Serial
    private static final long serialVersionUID = -8690976397610314400L;

    @JsonProperty("r")
    private Integer red;

    @JsonProperty("g")
    private Integer green;

    @JsonProperty("b")
    private Integer blue;
}
