package cn.jzyunqi.common.third.weixin.common.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;

import java.time.format.DateTimeFormatter;

/**
 * @author wiiyaya
 * @since 2024/10/11
 */
public class WxFormatUtils {

    public static final ObjectMapper OBJECT_MAPPER;

    static {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        ZonedDateTimeSerializer zonedDateTimeSerializer = new ZonedDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX"));
        javaTimeModule.addSerializer(zonedDateTimeSerializer);
        OBJECT_MAPPER = JsonMapper.builder()
                .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
                .addModule(javaTimeModule)
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .enable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .build();
    }
}
