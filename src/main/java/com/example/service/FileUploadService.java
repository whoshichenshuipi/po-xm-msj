package com.example.service;

import com.example.dto.FileUploadResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * 文件上传服务接口
 * 
 * @author example
 * @since 1.0.0
 */
public interface FileUploadService {

    /**
     * 上传文件到OSS
     * 
     * @param file 上传的文件
     * @param folder 存储文件夹
     * @return 上传结果
     */
    FileUploadResponse uploadFile(MultipartFile file, String folder);

    /**
     * 上传文件流到OSS
     * 
     * @param inputStream 文件输入流
     * @param fileName 文件名
     * @param contentType 文件类型
     * @param folder 存储文件夹
     * @return 上传结果
     */
    FileUploadResponse uploadFileStream(InputStream inputStream, String fileName, String contentType, String folder);

    /**
     * 上传Base64图片
     * 
     * @param base64Data Base64数据
     * @param fileName 文件名
     * @param folder 存储文件夹
     * @return 上传结果
     */
    FileUploadResponse uploadBase64Image(String base64Data, String fileName, String folder);

    /**
     * 删除OSS文件
     * 
     * @param filePath 文件路径
     * @return 是否删除成功
     */
    boolean deleteFile(String filePath);

    /**
     * 检查文件是否存在
     * 
     * @param filePath 文件路径
     * @return 是否存在
     */
    boolean fileExists(String filePath);

    /**
     * 获取文件访问URL
     * 
     * @param filePath 文件路径
     * @return 访问URL
     */
    String getFileUrl(String filePath);

    /**
     * 生成唯一文件名
     * 
     * @param originalFileName 原始文件名
     * @return 唯一文件名
     */
    String generateUniqueFileName(String originalFileName);
}
