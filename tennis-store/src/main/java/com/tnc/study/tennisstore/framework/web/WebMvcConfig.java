package com.tnc.study.tennisstore.framework.web;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * HTTP 메세지를 컨버팅 할 때 사용할 ObjectMapper
     * 굳이 따로 만든 이유는 API 테스트코드 작성 시 필요하기 떄문
     * @return
     */
    @Bean
    public ApiObjectMapper apiObjectMapper() {
        ApiObjectMapper objectMapper = new ApiObjectMapper();

        // 특정 필드가 http 요청으로 넘어왔지만 class field에는 없을 경우 실패하게 할지 정하는 옵션
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // http 요청의 특정 필드가 enum과 맵핑되는데, 해당 값이 enum으로 컨버팅 할 수 없으면 NULL 값 처리할 지 여부
        objectMapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);

        // enum으로 컨버팅 할 때, 기본적으로 요청 값을 enum name() 메서드의 반환 값과 비교해서 맵핑 해준다.
        // 하지만 READ_ENUMS_USING_TO_STRING 속성을 true로 하면 name() 메서드 대신 toString() 메서드의 반환 값과 맵핑 해준다.
        // DB 코드 값이 숫자로 시작하는 경우에 사용하면 유용하다. enum의 열거 상수를 숫자로 시작할 수 없기에 "CODE_10", "CODE_20" 같이 정의하고
        // "code" 필드 값을 "10", "20"으로 정의한다. 그리고 toString() 메서드를 overriding해서 "code" 필드 값을 반환하도록 한다.
        // 참고로 enum의 toString() 메서드는 기본적으로 name() 메서드의 결과 값을 반환하니깐 name() 메서드의 반환 값과
        // "code" 필드 값이 같은 경우는 overriding 할 필요가 없다.
        objectMapper.configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, true);

        // http message 중 null 혹은 비어있는 항목은 변환 하지 않는다.
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        // objectMapper가 LocalDate, LocalDateTime 타입의 property를 serialize, deserialize 할 때 필요한 옵션
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter datetimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(dateFormat));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(dateFormat));
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(datetimeFormat));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(datetimeFormat));
        objectMapper.registerModule(javaTimeModule);

        return objectMapper;
    }

    /**
     * 스프링 Web이 HttpMessage를 컨버팅할 때 사용할 Bean 객체
     * @return
     */
    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        return new MappingJackson2HttpMessageConverter(apiObjectMapper());
    }

    /**
     * LocalDate 타입을 Serialize
     */
    static class LocalDateSerializer extends JsonSerializer<LocalDate> {
        private final DateTimeFormatter dateFormat;
        public LocalDateSerializer(DateTimeFormatter dateFormat) {
            this.dateFormat = dateFormat;
        }
        @Override
        public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(value.format(dateFormat));
        }
    }

    /**
     * LocalDate 타입을 Deserialize
     */
    static class LocalDateDeserializer extends JsonDeserializer<LocalDate> {
        private final DateTimeFormatter dateFormat;
        public LocalDateDeserializer(DateTimeFormatter dateFormat) {
            this.dateFormat = dateFormat;
        }
        @Override
        public LocalDate deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
            return LocalDate.parse(p.getValueAsString(), dateFormat);
        }
    }

    /**
     * LocalDateTime 타입을 Serialize
     */
    static class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {
        private final DateTimeFormatter datetimeFormat;
        public LocalDateTimeSerializer(DateTimeFormatter datetimeFormat) {
            this.datetimeFormat = datetimeFormat;
        }
        @Override
        public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(value.format(datetimeFormat));
        }
    }

    /**
     * LocalDateTime 타입을 Deserialize
     */
    static class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
        private final DateTimeFormatter datetimeFormat;
        public LocalDateTimeDeserializer(DateTimeFormatter datetimeFormat) {
            this.datetimeFormat = datetimeFormat;
        }
        @Override
        public LocalDateTime deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
            return LocalDateTime.parse(p.getValueAsString(), datetimeFormat);
        }
    }
}
