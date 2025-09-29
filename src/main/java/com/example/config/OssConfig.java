package com.example.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 阿里云OSS配置类
 * 
 * @author example
 * @since 1.0.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "aliyun.oss")
public class OssConfig {

    /**
     * OSS访问域名
     */
    private String endpoint;

    /**
     * AccessKey ID
     */
    private String accessKeyId;

    /**
     * AccessKey Secret
     */
    private String accessKeySecret;

    /**
     * 存储桶名称
     */
    private String bucketName;

    /**
     * 访问域名
     */
    private String domain;

    /**
     * 上传配置
     */
    private UploadConfig upload = new UploadConfig();

    @Data
    public static class UploadConfig {
        /**
         * 最大文件大小
         */
        private String maxFileSize = "10MB";

        /**
         * 最大请求大小
         */
        private String maxRequestSize = "50MB";

        /**
         * 允许的文件扩展名
         */
        private String allowedExtensions = "jpg,jpeg,png,gif,bmp,webp";

        /**
         * 文件存储路径配置
         */
        private Map<String, String> paths;
    }
}
