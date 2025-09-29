package com.example.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Jackson时间格式配置
 * 用于统一处理LocalDateTime的序列化和反序列化
 */
@Configuration
public class JacksonConfig {

    /**
     * 时间格式：yyyy-MM-dd HH:mm:ss
     */
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    
    /**
     * 时间格式化器
     */
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        
        // 创建JavaTimeModule
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        
        // 配置LocalDateTime的序列化和反序列化
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DATE_TIME_FORMATTER));
        
        // 自定义反序列化器，支持多种格式
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DATE_TIME_FORMATTER) {
            @Override
            public LocalDateTime deserialize(com.fasterxml.jackson.core.JsonParser p, com.fasterxml.jackson.databind.DeserializationContext ctxt) throws java.io.IOException {
                String dateTimeString = p.getValueAsString();
                if (dateTimeString == null || dateTimeString.trim().isEmpty()) {
                    return null;
                }
                
                try {
                    // 尝试解析 "yyyy-MM-dd HH:mm:ss" 格式
                    return LocalDateTime.parse(dateTimeString, DATE_TIME_FORMATTER);
                } catch (DateTimeParseException e1) {
                    try {
                        // 尝试解析 ISO 格式 "yyyy-MM-ddTHH:mm:ss"
                        return LocalDateTime.parse(dateTimeString);
                    } catch (DateTimeParseException e2) {
                        try {
                            // 尝试解析 "yyyy-MM-dd" 格式，时间设为00:00:00
                            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                            return LocalDateTime.parse(dateTimeString, dateFormatter);
                        } catch (DateTimeParseException e3) {
                            try {
                                // 尝试解析 "yyyy-MM-dd HH:mm" 格式
                                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                                return LocalDateTime.parse(dateTimeString, dateTimeFormatter);
                            } catch (DateTimeParseException e4) {
                                // 如果所有格式都失败，使用当前时间
                                return LocalDateTime.now();
                            }
                        }
                    }
                }
            }
        });
        
        // 注册模块
        mapper.registerModule(javaTimeModule);
        
        // 禁用将日期写为时间戳
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        
        // 忽略未知属性
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        
        // 允许空字符串转换为null
        mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        
        return mapper;
    }
}
