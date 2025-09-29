package com.example.service;

import com.example.entity.CulturalContent;
import com.example.entity.CulturalContent.ApprovalStatus;
import com.example.entity.CulturalType;
import com.example.repository.CulturalContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class CulturalImportExportService {

    @Autowired
    private CulturalContentRepository contentRepository;

    /**
     * 导出文化内容为CSV格式
     */
    @Transactional(readOnly = true)
    public byte[] exportToCSV(Map<String, Object> exportParams) {
        try {
            List<CulturalContent> contents = getContentsForExport(exportParams);
            
            StringBuilder csv = new StringBuilder();
            // CSV头部
            csv.append("ID,标题,摘要,内容,类型,标签,商户ID,分类ID,审核状态,审核人ID,审核时间,审核意见,提交人ID,浏览量,点赞数,创建时间,更新时间\n");
            
            // CSV数据
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            for (CulturalContent content : contents) {
                csv.append(content.getId()).append(",");
                csv.append(escapeCsvField(content.getTitle())).append(",");
                csv.append(escapeCsvField(content.getSummary())).append(",");
                csv.append(escapeCsvField(content.getContent())).append(",");
                csv.append(content.getType()).append(",");
                csv.append(escapeCsvField(content.getTags())).append(",");
                csv.append(content.getMerchantId() != null ? content.getMerchantId() : "").append(",");
                csv.append(content.getCategoryId() != null ? content.getCategoryId() : "").append(",");
                csv.append(content.getApprovalStatus() != null ? content.getApprovalStatus() : "").append(",");
                csv.append(content.getApproverId() != null ? content.getApproverId() : "").append(",");
                csv.append(content.getApprovalTime() != null ? content.getApprovalTime().format(formatter) : "").append(",");
                csv.append(escapeCsvField(content.getApprovalComment())).append(",");
                csv.append(content.getSubmitterId() != null ? content.getSubmitterId() : "").append(",");
                csv.append(content.getViewCount() != null ? content.getViewCount() : 0).append(",");
                csv.append(content.getLikeCount() != null ? content.getLikeCount() : 0).append(",");
                csv.append(content.getCreatedAt() != null ? content.getCreatedAt().format(formatter) : "").append(",");
                csv.append(content.getUpdatedAt() != null ? content.getUpdatedAt().format(formatter) : "").append("\n");
            }
            
            return csv.toString().getBytes(StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("导出CSV失败: " + e.getMessage(), e);
        }
    }

    /**
     * 导出文化内容为JSON格式
     */
    @Transactional(readOnly = true)
    public String exportToJSON(Map<String, Object> exportParams) {
        try {
            List<CulturalContent> contents = getContentsForExport(exportParams);
            
            StringBuilder json = new StringBuilder();
            json.append("{\n");
            json.append("  \"exportTime\": \"").append(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)).append("\",\n");
            json.append("  \"totalCount\": ").append(contents.size()).append(",\n");
            json.append("  \"contents\": [\n");
            
            for (int i = 0; i < contents.size(); i++) {
                CulturalContent content = contents.get(i);
                json.append("    {\n");
                json.append("      \"id\": ").append(content.getId()).append(",\n");
                json.append("      \"title\": \"").append(escapeJsonField(content.getTitle())).append("\",\n");
                json.append("      \"summary\": \"").append(escapeJsonField(content.getSummary())).append("\",\n");
                json.append("      \"content\": \"").append(escapeJsonField(content.getContent())).append("\",\n");
                json.append("      \"type\": \"").append(content.getType()).append("\",\n");
                json.append("      \"tags\": \"").append(escapeJsonField(content.getTags())).append("\",\n");
                json.append("      \"merchantId\": ").append(content.getMerchantId() != null ? content.getMerchantId() : "null").append(",\n");
                json.append("      \"categoryId\": ").append(content.getCategoryId() != null ? content.getCategoryId() : "null").append(",\n");
                json.append("      \"approvalStatus\": \"").append(content.getApprovalStatus() != null ? content.getApprovalStatus() : "").append("\",\n");
                json.append("      \"approverId\": ").append(content.getApproverId() != null ? content.getApproverId() : "null").append(",\n");
                json.append("      \"approvalTime\": \"").append(content.getApprovalTime() != null ? content.getApprovalTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : "").append("\",\n");
                json.append("      \"approvalComment\": \"").append(escapeJsonField(content.getApprovalComment())).append("\",\n");
                json.append("      \"submitterId\": ").append(content.getSubmitterId() != null ? content.getSubmitterId() : "null").append(",\n");
                json.append("      \"viewCount\": ").append(content.getViewCount() != null ? content.getViewCount() : 0).append(",\n");
                json.append("      \"likeCount\": ").append(content.getLikeCount() != null ? content.getLikeCount() : 0).append(",\n");
                json.append("      \"createdAt\": \"").append(content.getCreatedAt() != null ? content.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : "").append("\",\n");
                json.append("      \"updatedAt\": \"").append(content.getUpdatedAt() != null ? content.getUpdatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : "").append("\n");
                json.append("    }");
                if (i < contents.size() - 1) {
                    json.append(",");
                }
                json.append("\n");
            }
            
            json.append("  ]\n");
            json.append("}\n");
            
            return json.toString();
        } catch (Exception e) {
            throw new RuntimeException("导出JSON失败: " + e.getMessage(), e);
        }
    }

    /**
     * 从CSV文件导入文化内容
     */
    public Map<String, Object> importFromCSV(MultipartFile file, boolean skipHeader) {
        Map<String, Object> result = new HashMap<>();
        List<String> errors = new ArrayList<>();
        int successCount = 0;
        int errorCount = 0;
        
        try {
            List<String> lines = readFileLines(file);
            
            int startLine = skipHeader ? 1 : 0;
            for (int i = startLine; i < lines.size(); i++) {
                try {
                    String line = lines.get(i);
                    if (line.trim().isEmpty()) continue;
                    
                    CulturalContent content = parseCSVLine(line);
                    if (content != null) {
                        contentRepository.save(content);
                        successCount++;
                    } else {
                        errors.add("第" + (i + 1) + "行: 数据格式错误");
                        errorCount++;
                    }
                } catch (Exception e) {
                    errors.add("第" + (i + 1) + "行: " + e.getMessage());
                    errorCount++;
                }
            }
            
        } catch (Exception e) {
            errors.add("文件读取失败: " + e.getMessage());
            errorCount++;
        }
        
        result.put("successCount", successCount);
        result.put("errorCount", errorCount);
        result.put("errors", errors);
        result.put("totalProcessed", successCount + errorCount);
        
        return result;
    }

    /**
     * 从JSON文件导入文化内容
     */
    public Map<String, Object> importFromJSON(MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        List<String> errors = new ArrayList<>();
        int successCount = 0;
        int errorCount = 0;
        
        try {
            String jsonContent = new String(file.getBytes(), StandardCharsets.UTF_8);
            // 这里简化处理，实际应该使用JSON解析库
            // 暂时返回基本结果
            result.put("successCount", 0);
            result.put("errorCount", 1);
            result.put("errors", Arrays.asList("JSON导入功能待实现"));
            result.put("totalProcessed", 1);
            
        } catch (Exception e) {
            errors.add("文件读取失败: " + e.getMessage());
            errorCount++;
            result.put("successCount", successCount);
            result.put("errorCount", errorCount);
            result.put("errors", errors);
            result.put("totalProcessed", successCount + errorCount);
        }
        
        return result;
    }

    /**
     * 获取导出数据
     */
    private List<CulturalContent> getContentsForExport(Map<String, Object> exportParams) {
        List<CulturalContent> contents;
        
        // 根据参数筛选内容
        String approvalStatusStr = (String) exportParams.get("approvalStatus");
        String typeStr = (String) exportParams.get("type");
        Boolean includeAll = (Boolean) exportParams.getOrDefault("includeAll", false);
        
        if (includeAll) {
            contents = contentRepository.findAll();
        } else if (approvalStatusStr != null) {
            try {
                ApprovalStatus status = ApprovalStatus.valueOf(approvalStatusStr);
                contents = contentRepository.findByApprovalStatus(status);
            } catch (IllegalArgumentException e) {
                contents = contentRepository.findByApprovedTrue();
            }
        } else if (typeStr != null) {
            try {
                CulturalType type = CulturalType.valueOf(typeStr);
                contents = contentRepository.findByTypeAndApprovedTrue(type);
            } catch (IllegalArgumentException e) {
                contents = contentRepository.findByApprovedTrue();
            }
        } else {
            contents = contentRepository.findByApprovedTrue();
        }
        
        return contents;
    }

    /**
     * 读取文件行
     */
    private List<String> readFileLines(MultipartFile file) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }

    /**
     * 解析CSV行
     */
    private CulturalContent parseCSVLine(String line) {
        try {
            String[] fields = parseCSVFields(line);
            if (fields.length < 17) {
                return null;
            }
            
            CulturalContent content = new CulturalContent();
            
            // 基本字段
            if (!fields[1].isEmpty()) content.setTitle(fields[1]);
            if (!fields[2].isEmpty()) content.setSummary(fields[2]);
            if (!fields[3].isEmpty()) content.setContent(fields[3]);
            
            // 类型
            if (!fields[4].isEmpty()) {
                try {
                    content.setType(CulturalType.valueOf(fields[4]));
                } catch (IllegalArgumentException e) {
                    return null;
                }
            }
            
            // 其他字段
            if (!fields[5].isEmpty()) content.setTags(fields[5]);
            if (!fields[6].isEmpty()) content.setMerchantId(Long.parseLong(fields[6]));
            if (!fields[7].isEmpty()) content.setCategoryId(Long.parseLong(fields[7]));
            
            // 审核状态
            if (!fields[8].isEmpty()) {
                try {
                    content.setApprovalStatus(ApprovalStatus.valueOf(fields[8]));
                } catch (IllegalArgumentException e) {
                    content.setApprovalStatus(ApprovalStatus.PENDING);
                }
            } else {
                content.setApprovalStatus(ApprovalStatus.PENDING);
            }
            
            if (!fields[9].isEmpty()) content.setApproverId(Long.parseLong(fields[9]));
            if (!fields[11].isEmpty()) content.setApprovalComment(fields[11]);
            if (!fields[12].isEmpty()) content.setSubmitterId(Long.parseLong(fields[12]));
            if (!fields[13].isEmpty()) content.setViewCount(Integer.parseInt(fields[13]));
            if (!fields[14].isEmpty()) content.setLikeCount(Integer.parseInt(fields[14]));
            
            // 设置默认值
            content.setApproved(content.getApprovalStatus() == ApprovalStatus.APPROVED);
            if (content.getViewCount() == null) content.setViewCount(0);
            if (content.getLikeCount() == null) content.setLikeCount(0);
            
            return content;
            
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 解析CSV字段
     */
    private String[] parseCSVFields(String line) {
        List<String> fields = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder currentField = new StringBuilder();
        
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                fields.add(currentField.toString());
                currentField = new StringBuilder();
            } else {
                currentField.append(c);
            }
        }
        
        fields.add(currentField.toString());
        return fields.toArray(new String[0]);
    }

    /**
     * 转义CSV字段
     */
    private String escapeCsvField(String field) {
        if (field == null) return "";
        if (field.contains(",") || field.contains("\"") || field.contains("\n")) {
            return "\"" + field.replace("\"", "\"\"") + "\"";
        }
        return field;
    }

    /**
     * 转义JSON字段
     */
    private String escapeJsonField(String field) {
        if (field == null) return "";
        return field.replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
    }
}
