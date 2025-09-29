package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文件上传响应DTO
 * 
 * @author example
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileUploadResponse {

    /**
     * 是否上传成功
     */
    private Boolean success;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 文件信息
     */
    private FileInfo fileInfo;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FileInfo {
        /**
         * 原始文件名
         */
        private String originalName;

        /**
         * 存储文件名
         */
        private String fileName;

        /**
         * 文件大小（字节）
         */
        private Long fileSize;

        /**
         * 文件类型
         */
        private String contentType;

        /**
         * 文件访问URL
         */
        private String fileUrl;

        /**
         * 文件存储路径
         */
        private String filePath;

        /**
         * 文件扩展名
         */
        private String fileExtension;
    }

    /**
     * 创建成功响应
     */
    public static FileUploadResponse success(FileInfo fileInfo) {
        return new FileUploadResponse(true, "文件上传成功", fileInfo);
    }

    /**
     * 创建失败响应
     */
    public static FileUploadResponse failure(String message) {
        return new FileUploadResponse(false, message, null);
    }
}
