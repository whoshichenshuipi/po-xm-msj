package com.example.controller;

import com.example.dto.FileUploadResponse;
import com.example.service.FileUploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * 文件上传控制器
 * 
 * @author example
 * @since 1.0.0
 */
@RestController
@RequestMapping("/upload")
@CrossOrigin(origins = "*")
@Tag(name = "文件上传", description = "阿里云OSS文件上传管理")
public class FileUploadController {

    @Autowired
    private FileUploadService fileUploadService;

    /**
     * 上传单个文件
     */
    @PostMapping("/file")
    @Operation(summary = "上传单个文件", description = "上传单个文件到阿里云OSS")
    public ResponseEntity<FileUploadResponse> uploadFile(
            @Parameter(description = "上传的文件", required = true)
            @RequestParam("file") MultipartFile file,
            @Parameter(description = "存储文件夹", example = "avatar")
            @RequestParam(value = "folder", defaultValue = "temp") String folder) {

        FileUploadResponse response = fileUploadService.uploadFile(file, folder);
        
        if (response.getSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 上传头像
     */
    @PostMapping("/avatar")
    @Operation(summary = "上传头像", description = "上传用户头像到OSS")
    public ResponseEntity<FileUploadResponse> uploadAvatar(
            @Parameter(description = "头像文件", required = true)
            @RequestParam("file") MultipartFile file) {

        FileUploadResponse response = fileUploadService.uploadFile(file, "avatar");
        
        if (response.getSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 上传商户图片
     */
    @PostMapping("/merchant")
    @Operation(summary = "上传商户图片", description = "上传商户相关图片到OSS")
    public ResponseEntity<FileUploadResponse> uploadMerchantImage(
            @Parameter(description = "商户图片文件", required = true)
            @RequestParam("file") MultipartFile file) {

        FileUploadResponse response = fileUploadService.uploadFile(file, "merchant");
        
        if (response.getSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 上传产品图片
     */
    @PostMapping("/product")
    @Operation(summary = "上传产品图片", description = "上传产品相关图片到OSS")
    public ResponseEntity<FileUploadResponse> uploadProductImage(
            @Parameter(description = "产品图片文件", required = true)
            @RequestParam("file") MultipartFile file) {

        FileUploadResponse response = fileUploadService.uploadFile(file, "product");
        
        if (response.getSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 上传文化内容图片
     */
    @PostMapping("/cultural")
    @Operation(summary = "上传文化内容图片", description = "上传文化内容相关图片到OSS")
    public ResponseEntity<FileUploadResponse> uploadCulturalImage(
            @Parameter(description = "文化内容图片文件", required = true)
            @RequestParam("file") MultipartFile file) {

        FileUploadResponse response = fileUploadService.uploadFile(file, "cultural");
        
        if (response.getSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 上传路线图片
     */
    @PostMapping("/route")
    @Operation(summary = "上传路线图片", description = "上传路线相关图片到OSS")
    public ResponseEntity<FileUploadResponse> uploadRouteImage(
            @Parameter(description = "路线图片文件", required = true)
            @RequestParam("file") MultipartFile file) {

        FileUploadResponse response = fileUploadService.uploadFile(file, "route");
        
        if (response.getSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 上传Base64图片
     */
    @PostMapping("/base64")
    @Operation(summary = "上传Base64图片", description = "上传Base64格式的图片到OSS")
    public ResponseEntity<FileUploadResponse> uploadBase64Image(
            @Parameter(description = "Base64图片数据", required = true)
            @RequestParam("base64Data") String base64Data,
            @Parameter(description = "文件名", required = true)
            @RequestParam("fileName") String fileName,
            @Parameter(description = "存储文件夹", example = "temp")
            @RequestParam(value = "folder", defaultValue = "temp") String folder) {

        FileUploadResponse response = fileUploadService.uploadBase64Image(base64Data, fileName, folder);
        
        if (response.getSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 删除文件
     */
    @DeleteMapping("/file")
    @Operation(summary = "删除文件", description = "从OSS删除指定文件")
    public ResponseEntity<Map<String, Object>> deleteFile(
            @Parameter(description = "文件路径", required = true)
            @RequestParam("filePath") String filePath) {

        boolean success = fileUploadService.deleteFile(filePath);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", success);
        result.put("message", success ? "文件删除成功" : "文件删除失败");
        
        if (success) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * 检查文件是否存在
     */
    @GetMapping("/exists")
    @Operation(summary = "检查文件是否存在", description = "检查OSS中文件是否存在")
    public ResponseEntity<Map<String, Object>> checkFileExists(
            @Parameter(description = "文件路径", required = true)
            @RequestParam("filePath") String filePath) {

        boolean exists = fileUploadService.fileExists(filePath);
        
        Map<String, Object> result = new HashMap<>();
        result.put("exists", exists);
        result.put("filePath", filePath);
        
        return ResponseEntity.ok(result);
    }

    /**
     * 获取文件访问URL
     */
    @GetMapping("/url")
    @Operation(summary = "获取文件访问URL", description = "获取OSS文件的访问URL")
    public ResponseEntity<Map<String, Object>> getFileUrl(
            @Parameter(description = "文件路径", required = true)
            @RequestParam("filePath") String filePath) {

        String fileUrl = fileUploadService.getFileUrl(filePath);
        
        Map<String, Object> result = new HashMap<>();
        result.put("filePath", filePath);
        result.put("fileUrl", fileUrl);
        
        return ResponseEntity.ok(result);
    }

    /**
     * 批量上传文件
     */
    @PostMapping("/batch")
    @Operation(summary = "批量上传文件", description = "批量上传多个文件到OSS")
    public ResponseEntity<Map<String, Object>> batchUploadFiles(
            @Parameter(description = "上传的文件列表", required = true)
            @RequestParam("files") MultipartFile[] files,
            @Parameter(description = "存储文件夹", example = "temp")
            @RequestParam(value = "folder", defaultValue = "temp") String folder) {

        Map<String, Object> result = new HashMap<>();
        FileUploadResponse[] responses = new FileUploadResponse[files.length];
        int successCount = 0;

        for (int i = 0; i < files.length; i++) {
            responses[i] = fileUploadService.uploadFile(files[i], folder);
            if (responses[i].getSuccess()) {
                successCount++;
            }
        }

        result.put("totalFiles", files.length);
        result.put("successCount", successCount);
        result.put("failureCount", files.length - successCount);
        result.put("responses", responses);

        return ResponseEntity.ok(result);
    }
}
