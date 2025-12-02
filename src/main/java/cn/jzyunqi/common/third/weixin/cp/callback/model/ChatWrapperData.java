package cn.jzyunqi.common.third.weixin.cp.callback.model;

import cn.jzyunqi.common.third.weixin.common.model.WeixinRspV1;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author wiiyaya
 * @since 2025/12/2
 */
@Getter
@Setter
@ToString
public class ChatWrapperData extends WeixinRspV1 {

    /**
     * 聊天信息
     */
    @JsonProperty("chatdata")
    private List<EncryptChatData> chatDataList;
}
