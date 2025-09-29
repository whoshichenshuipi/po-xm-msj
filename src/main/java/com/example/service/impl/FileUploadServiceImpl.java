package com.example.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.ObjectMetadata;
import com.example.config.OssConfig;
import com.example.dto.FileUploadResponse;
import com.example.service.FileUploadService;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.UUID;

/**
 * 文件上传服务实现类
 * 
 * @author example
 * @since 1.0.0
 */
@Service
public class FileUploadServiceImpl implements FileUploadService {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadServiceImpl.class);

    @Autowired
    private OSS ossClient;

    @Autowired
    private OssConfig ossConfig;

    @Override
    public FileUploadResponse uploadFile(MultipartFile file, String folder) {
        try {
            // 验证文件
            if (file == null || file.isEmpty()) {
                return FileUploadResponse.failure("文件不能为空");
            }

            // 验证文件类型
            if (!isAllowedFileType(file.getOriginalFilename())) {
                return FileUploadResponse.failure("不支持的文件类型");
            }

            // 验证文件大小
            if (file.getSize() > parseFileSize(ossConfig.getUpload().getMaxFileSize())) {
                return FileUploadResponse.failure("文件大小超出限制");
            }

            // 生成文件名
            String originalFileName = file.getOriginalFilename();
            String fileName = generateUniqueFileName(originalFileName);
            String filePath = buildFilePath(folder, fileName);

            // 设置文件元数据
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());
            metadata.setContentDisposition("inline");

            // 上传文件
            ossClient.putObject(ossConfig.getBucketName(), filePath, file.getInputStream(), metadata);

            // 构建文件信息
            FileUploadResponse.FileInfo fileInfo = new FileUploadResponse.FileInfo();
            fileInfo.setOriginalName(originalFileName);
            fileInfo.setFileName(fileName);
            fileInfo.setFileSize(file.getSize());
            fileInfo.setContentType(file.getContentType());
            fileInfo.setFilePath(filePath);
            fileInfo.setFileUrl(getFileUrl(filePath));
            fileInfo.setFileExtension(getFileExtension(originalFileName));

            logger.info("文件上传成功: {}", filePath);
            return FileUploadResponse.success(fileInfo);

        } catch (IOException e) {
            logger.error("文件上传失败", e);
            return FileUploadResponse.failure("文件上传失败: " + e.getMessage());
        } catch (Exception e) {
            logger.error("文件上传异常", e);
            return FileUploadResponse.failure("文件上传异常: " + e.getMessage());
        }
    }

    @Override
    public FileUploadResponse uploadFileStream(InputStream inputStream, String fileName, String contentType, String folder) {
        try {
            // 验证输入流
            if (inputStream == null) {
                return FileUploadResponse.failure("文件流不能为空");
            }

            // 验证文件类型
            if (!isAllowedFileType(fileName)) {
                return FileUploadResponse.failure("不支持的文件类型");
            }

            // 生成唯一文件名
            String uniqueFileName = generateUniqueFileName(fileName);
            String filePath = buildFilePath(folder, uniqueFileName);

            // 设置文件元数据
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(contentType);
            metadata.setContentDisposition("inline");

            // 上传文件
            ossClient.putObject(ossConfig.getBucketName(), filePath, inputStream, metadata);

            // 构建文件信息
            FileUploadResponse.FileInfo fileInfo = new FileUploadResponse.FileInfo();
            fileInfo.setOriginalName(fileName);
            fileInfo.setFileName(uniqueFileName);
            fileInfo.setContentType(contentType);
            fileInfo.setFilePath(filePath);
            fileInfo.setFileUrl(getFileUrl(filePath));
            fileInfo.setFileExtension(getFileExtension(fileName));

            logger.info("文件流上传成功: {}", filePath);
            return FileUploadResponse.success(fileInfo);

        } catch (Exception e) {
            logger.error("文件流上传异常", e);
            return FileUploadResponse.failure("文件流上传异常: " + e.getMessage());
        }
    }

    @Override
    public FileUploadResponse uploadBase64Image(String base64Data, String fileName, String folder) {
        try {
            // 验证Base64数据
            if (!StringUtils.hasText(base64Data)) {
                return FileUploadResponse.failure("Base64数据不能为空");
            }

            // 解析Base64数据
            String[] parts = base64Data.split(",");
            if (parts.length != 2) {
                return FileUploadResponse.failure("Base64数据格式错误");
            }

            String contentType = getContentTypeFromBase64(parts[0]);
            byte[] imageBytes = java.util.Base64.getDecoder().decode(parts[1]);

            // 验证文件大小
            if (imageBytes.length > parseFileSize(ossConfig.getUpload().getMaxFileSize())) {
                return FileUploadResponse.failure("文件大小超出限制");
            }

            // 生成文件名
            String uniqueFileName = generateUniqueFileName(fileName);
            String filePath = buildFilePath(folder, uniqueFileName);

            // 设置文件元数据
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(imageBytes.length);
            metadata.setContentType(contentType);
            metadata.setContentDisposition("inline");

            // 上传文件
            ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
            ossClient.putObject(ossConfig.getBucketName(), filePath, inputStream, metadata);

            // 构建文件信息
            FileUploadResponse.FileInfo fileInfo = new FileUploadResponse.FileInfo();
            fileInfo.setOriginalName(fileName);
            fileInfo.setFileName(uniqueFileName);
            fileInfo.setFileSize((long) imageBytes.length);
            fileInfo.setContentType(contentType);
            fileInfo.setFilePath(filePath);
            fileInfo.setFileUrl(getFileUrl(filePath));
            fileInfo.setFileExtension(getFileExtension(fileName));

            logger.info("Base64图片上传成功: {}", filePath);
            return FileUploadResponse.success(fileInfo);

        } catch (Exception e) {
            logger.error("Base64图片上传异常", e);
            return FileUploadResponse.failure("Base64图片上传异常: " + e.getMessage());
        }
    }

    @Override
    public boolean deleteFile(String filePath) {
        try {
            if (!StringUtils.hasText(filePath)) {
                return false;
            }

            ossClient.deleteObject(ossConfig.getBucketName(), filePath);
            logger.info("文件删除成功: {}", filePath);
            return true;

        } catch (Exception e) {
            logger.error("文件删除失败: {}", filePath, e);
            return false;
        }
    }

    @Override
    public boolean fileExists(String filePath) {
        try {
            if (!StringUtils.hasText(filePath)) {
                return false;
            }

            return ossClient.doesObjectExist(ossConfig.getBucketName(), filePath);

        } catch (Exception e) {
            logger.error("检查文件是否存在失败: {}", filePath, e);
            return false;
        }
    }

    @Override
    public String getFileUrl(String filePath) {
        if (!StringUtils.hasText(filePath)) {
            return null;
        }

        return ossConfig.getDomain() + "/" + filePath;
    }

    @Override
    public String generateUniqueFileName(String originalFileName) {
        if (!StringUtils.hasText(originalFileName)) {
            return UUID.randomUUID().toString();
        }

        String extension = getFileExtension(originalFileName);
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 8);

        return timestamp + "_" + uuid + "." + extension;
    }

    /**
     * 验证文件类型是否允许
     */
    private boolean isAllowedFileType(String fileName) {
        if (!StringUtils.hasText(fileName)) {
            return false;
        }

        String extension = getFileExtension(fileName).toLowerCase();
        String[] allowedExtensions = ossConfig.getUpload().getAllowedExtensions().split(",");

        return Arrays.asList(allowedExtensions).contains(extension);
    }

    /**
     * 获取文件扩展名
     */
    private String getFileExtension(String fileName) {
        if (!StringUtils.hasText(fileName)) {
            return "";
        }

        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex == -1) {
            return "";
        }

        return fileName.substring(lastDotIndex + 1);
    }

    /**
     * 构建文件路径
     */
    private String buildFilePath(String folder, String fileName) {
        if (!StringUtils.hasText(folder)) {
            folder = "temp";
        }

        // 确保文件夹路径以/结尾
        if (!folder.endsWith("/")) {
            folder += "/";
        }

        return folder + fileName;
    }

    /**
     * 解析文件大小字符串为字节数
     */
    private long parseFileSize(String sizeStr) {
        if (!StringUtils.hasText(sizeStr)) {
            return 10 * 1024 * 1024; // 默认10MB
        }

        sizeStr = sizeStr.toUpperCase();
        if (sizeStr.endsWith("KB")) {
            return Long.parseLong(sizeStr.replace("KB", "")) * 1024;
        } else if (sizeStr.endsWith("MB")) {
            return Long.parseLong(sizeStr.replace("MB", "")) * 1024 * 1024;
        } else if (sizeStr.endsWith("GB")) {
            return Long.parseLong(sizeStr.replace("GB", "")) * 1024 * 1024 * 1024;
        } else {
            return Long.parseLong(sizeStr);
        }
    }

    /**
     * 从Base64头部获取ContentType
     */
    private String getContentTypeFromBase64(String header) {
        if (header.contains("image/jpeg")) {
            return "image/jpeg";
        } else if (header.contains("image/png")) {
            return "image/png";
        } else if (header.contains("image/gif")) {
            return "image/gif";
        } else if (header.contains("image/webp")) {
            return "image/webp";
        } else {
            return "image/jpeg"; // 默认
        }
    }
}
