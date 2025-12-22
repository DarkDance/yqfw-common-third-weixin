package cn.jzyunqi.common.third.weixin.common.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ClientCodecConfigurer;
import org.springframework.http.codec.json.JacksonJsonDecoder;
import org.springframework.http.codec.json.JacksonJsonEncoder;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.MapperFeature;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.cfg.DateTimeFeature;
import tools.jackson.databind.json.JsonMapper;

/**
 * @author wiiyaya
 * @since 2024/10/11
 */
public class WxFormatUtils {

    public static final JsonMapper JSON_MAPPER;

    static {
        JSON_MAPPER = JsonMapper.builder()
                .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE) // 驼峰转下划线
                .changeDefaultPropertyInclusion(incl -> incl.withValueInclusion(JsonInclude.Include.NON_NULL)) // 序列化时不输出null属性
                .enable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY) // 排序属性
                .disable(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS) // 禁用时间戳
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES) // 反序列化时忽略未知属性
                .build();
    }

    public static void jackson2Config(ClientCodecConfigurer codecConfig) {
        codecConfig.defaultCodecs().jacksonJsonEncoder(new JacksonJsonEncoder(WxFormatUtils.JSON_MAPPER));
        codecConfig.defaultCodecs().jacksonJsonDecoder(new JacksonJsonDecoder(WxFormatUtils.JSON_MAPPER));
    }

    public static void jackson2ConfigSpecial(ClientCodecConfigurer codecConfig) {
        codecConfig.defaultCodecs().jacksonJsonEncoder(new JacksonJsonEncoder(WxFormatUtils.JSON_MAPPER));
        codecConfig.defaultCodecs().jacksonJsonDecoder(new JacksonJsonDecoder(WxFormatUtils.JSON_MAPPER, MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN));
    }
}
