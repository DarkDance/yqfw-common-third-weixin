package cn.jzyunqi.common.third.weixin.mp.callback.model;

import cn.jzyunqi.common.third.weixin.mp.material.model.WxMpItemData;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import java.io.Serializable;
import java.util.List;

/**
 * @author wiiyaya
 * @since 2018/8/19.
 */

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class ItemListParam implements Serializable {
    private static final long serialVersionUID = -6253523499813424333L;

    @JsonProperty("articles")
    @XmlElement(name = "item")
    private List<WxMpItemData> articles;
}