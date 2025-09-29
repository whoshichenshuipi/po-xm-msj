package com.example.controller;

import com.example.service.CulturalImportExportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/cultural-import-export")
@CrossOrigin(origins = "*")
@Slf4j
@Tag(name = "文化内容导入导出", description = "文化内容的批量导入和导出功能")
public class CulturalImportExportController {

    @Autowired
    private CulturalImportExportService importExportService;

    @Operation(summary = "导出文化内容为CSV格式")
    @PostMapping("/export/csv")
    public ResponseEntity<byte[]> exportToCSV(@RequestBody Map<String, Object> exportParams) {
        log.info("导出文化内容为CSV格式，参数: {}", exportParams);
        try {
            byte[] csvData = importExportService.exportToCSV(exportParams);
            
            String filename = "cultural_content_" + 
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".csv";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", filename);
            headers.setContentLength(csvData.length);
            
            return new ResponseEntity<>(csvData, headers, HttpStatus.OK);
            
        } catch (Exception e) {
            log.error("导出CSV失败", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "导出文化内容为JSON格式")
    @PostMapping("/export/json")
    public ResponseEntity<String> exportToJSON(@RequestBody Map<String, Object> exportParams) {
        log.info("导出文化内容为JSON格式，参数: {}", exportParams);
        try {
            String jsonData = importExportService.exportToJSON(exportParams);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            return new ResponseEntity<>(jsonData, headers, HttpStatus.OK);
            
        } catch (Exception e) {
            log.error("导出JSON失败", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "导出失败");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse.toString());
        }
    }

    @Operation(summary = "从CSV文件导入文化内容")
    @PostMapping("/import/csv")
    public ResponseEntity<?> importFromCSV(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "skipHeader", defaultValue = "true") boolean skipHeader) {
        log.info("从CSV文件导入文化内容，文件名: {}, 跳过标题行: {}", file.getOriginalFilename(), skipHeader);
        
        if (file.isEmpty()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "文件为空");
            return ResponseEntity.badRequest().body(errorResponse);
        }
        
        try {
            Map<String, Object> result = importExportService.importFromCSV(file, skipHeader);
            log.info("CSV导入完成，成功: {}, 失败: {}", result.get("successCount"), result.get("errorCount"));
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            log.error("CSV导入失败", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "导入失败");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(summary = "从JSON文件导入文化内容")
    @PostMapping("/import/json")
    public ResponseEntity<?> importFromJSON(@RequestParam("file") MultipartFile file) {
        log.info("从JSON文件导入文化内容，文件名: {}", file.getOriginalFilename());
        
        if (file.isEmpty()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "文件为空");
            return ResponseEntity.badRequest().body(errorResponse);
        }
        
        try {
            Map<String, Object> result = importExportService.importFromJSON(file);
            log.info("JSON导入完成，成功: {}, 失败: {}", result.get("successCount"), result.get("errorCount"));
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            log.error("JSON导入失败", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "导入失败");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(summary = "获取导入导出模板")
    @GetMapping("/template/csv")
    public ResponseEntity<byte[]> getCSVTemplate() {
        log.info("获取CSV导入模板");
        try {
            String template = "标题,摘要,内容,类型,标签,商户ID,分类ID,审核状态,审核人ID,审核时间,审核意见,提交人ID,浏览量,点赞数\n" +
                    "示例标题,示例摘要,示例内容,FOOD_CULTURE,示例标签,1,1,PENDING,,,示例审核意见,1,0,0";
            
            byte[] templateData = template.getBytes("UTF-8");
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "cultural_content_template.csv");
            headers.setContentLength(templateData.length);
            
            return new ResponseEntity<>(templateData, headers, HttpStatus.OK);
            
        } catch (Exception e) {
            log.error("获取CSV模板失败", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "获取导入导出说明")
    @GetMapping("/help")
    public ResponseEntity<?> getImportExportHelp() {
        Map<String, Object> help = new HashMap<>();
        
        Map<String, Object> csvHelp = new HashMap<>();
        csvHelp.put("format", "CSV");
        csvHelp.put("description", "逗号分隔值格式，支持Excel打开");
        csvHelp.put("fields", new String[]{
            "标题", "摘要", "内容", "类型", "标签", "商户ID", "分类ID", 
            "审核状态", "审核人ID", "审核时间", "审核意见", "提交人ID", "浏览量", "点赞数"
        });
        csvHelp.put("requiredFields", new String[]{"标题", "类型"});
        csvHelp.put("typeValues", new String[]{"FOOD_CULTURE", "FOLK_CUSTOM", "CRAFT_SKILL", "EVENT_HISTORY"});
        csvHelp.put("statusValues", new String[]{"PENDING", "APPROVED", "REJECTED"});
        
        Map<String, Object> jsonHelp = new HashMap<>();
        jsonHelp.put("format", "JSON");
        jsonHelp.put("description", "JSON格式，支持复杂数据结构");
        jsonHelp.put("note", "JSON导入功能正在开发中");
        
        help.put("csv", csvHelp);
        help.put("json", jsonHelp);
        help.put("tips", new String[]{
            "导入前请确保数据格式正确",
            "必填字段不能为空",
            "类型和审核状态必须使用预定义的值",
            "建议先下载模板文件",
            "大量数据导入可能需要较长时间"
        });
        
        return ResponseEntity.ok(help);
    }
}
