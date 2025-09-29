package com.example.controller;

import com.example.entity.CulturalContent;
import com.example.entity.CulturalContent.ApprovalStatus;
import com.example.entity.CulturalType;
import com.example.service.CulturalContentService;
import com.example.service.CulturalRecommendationService;
import com.example.service.CulturalSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/cultural")
@CrossOrigin(origins = "*")
@Slf4j
@Tag(name = "文化内容管理", description = "食品/民俗/技艺等文化内容的管理与查询")
public class CulturalContentController {

    @Autowired
    private CulturalContentService service;

    @Autowired
    private CulturalRecommendationService recommendationService;

    @Autowired
    private CulturalSearchService searchService;

    @Operation(summary = "创建文化内容（默认待审核）")
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CulturalContent content, BindingResult bindingResult, HttpServletRequest request) {
        log.info("接收到创建文化内容请求: {}", content);
        
        // 验证失败处理
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> {
                errors.put(error.getField(), error.getDefaultMessage());
            });
            log.warn("文化内容创建验证失败: {}", errors);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "数据验证失败");
            errorResponse.put("details", errors);
            return ResponseEntity.badRequest().body(errorResponse);
        }
        
        try {
            // 数据预处理
            if (content.getId() != null) {
                content.setId(null); // 创建时强制ID为null
            }
            
            // 验证必需字段
            if (content.getTitle() == null || content.getTitle().trim().isEmpty()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("error", "标题不能为空");
                return ResponseEntity.badRequest().body(errorResponse);
            }
            
            if (content.getType() == null) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("error", "文化内容类型不能为空");
                errorResponse.put("supportedTypes", new String[]{"FOOD_CULTURE", "FOLK_CUSTOM", "CRAFT_SKILL", "EVENT_HISTORY"});
                return ResponseEntity.badRequest().body(errorResponse);
            }
            
            // 获取当前用户ID（这里简化处理，实际应该从Session中获取）
            Long currentUserId = getCurrentUserId(request);
            if (currentUserId != null) {
                content.setSubmitterId(currentUserId);
            }
            
            CulturalContent created = service.create(content);
            log.info("文化内容创建成功，ID: {}", created.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
            
        } catch (Exception e) {
            log.error("创建文化内容时发生错误", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "服务器内部错误");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(summary = "根据ID获取文化内容")
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Optional<CulturalContent> content = service.findById(id);
        return content.<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "列出文化内容（支持分页和筛选）")
    @GetMapping
    public ResponseEntity<?> list(
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) CulturalType type,
            @RequestParam(required = false) Boolean approved,
            @RequestParam(required = false) ApprovalStatus approvalStatus,
            @RequestParam(required = false) String tag) {
        log.info("获取文化内容列表，页码: {}, 页大小: {}, 关键词: {}, 类型: {}, 审核状态: {}, 标签: {}", 
                pageNo, pageSize, keyword, type, approved, approvalStatus, tag);
        
        try {
            List<CulturalContent> contents;
            
            // 根据参数筛选内容
            if (keyword != null && !keyword.trim().isEmpty()) {
                if (approvalStatus != null) {
                    contents = service.searchByApprovalStatus(approvalStatus, keyword);
                } else {
                    contents = service.search(keyword);
                }
            } else if (tag != null && !tag.trim().isEmpty()) {
                if (approvalStatus != null) {
                    contents = service.filterByApprovalStatusAndTag(approvalStatus, tag);
                } else {
                    contents = service.filterByTag(tag);
                }
            } else if (type != null && approvalStatus != null) {
                contents = service.listByApprovalStatusAndType(approvalStatus, type);
            } else if (approvalStatus != null) {
                contents = service.listByApprovalStatus(approvalStatus);
            } else if (type != null) {
                contents = service.listByType(type);
            } else if (approved != null) {
                contents = approved ? service.listAllApproved() : service.listAllPending();
            } else {
                contents = service.listAll();
            }
            
            // 简单的分页处理
            int start = (pageNo - 1) * pageSize;
            int end = Math.min(start + pageSize, contents.size());
            
            if (start >= contents.size()) {
                Map<String, Object> response = new HashMap<>();
                response.put("records", new ArrayList<>());
                response.put("total", contents.size());
                response.put("pageNo", pageNo);
                response.put("pageSize", pageSize);
                return ResponseEntity.ok(response);
            }
            
            List<CulturalContent> paginatedContents = contents.subList(start, end);
            
            Map<String, Object> response = new HashMap<>();
            response.put("records", paginatedContents);
            response.put("total", contents.size());
            response.put("pageNo", pageNo);
            response.put("pageSize", pageSize);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("获取文化内容列表时发生错误", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "获取数据失败");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(summary = "获取已审核的文化内容（分页）")
    @GetMapping("/approved")
    public ResponseEntity<List<CulturalContent>> getApproved(
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) {
        log.info("获取已审核文化内容，页码: {}, 页大小: {}", pageNo, pageSize);
        List<CulturalContent> approvedContents = service.listAllApproved();
        
        // 简单的分页处理（实际项目中应该在Service层使用Page对象）
        int start = (pageNo - 1) * pageSize;
        int end = Math.min(start + pageSize, approvedContents.size());
        
        if (start >= approvedContents.size()) {
            return ResponseEntity.ok(new ArrayList<>());
        }
        
        List<CulturalContent> paginatedContents = approvedContents.subList(start, end);
        return ResponseEntity.ok(paginatedContents);
    }

    @Operation(summary = "按类型列出已审核内容")
    @GetMapping("/type/{type}")
    public ResponseEntity<List<CulturalContent>> listByType(@PathVariable CulturalType type) {
        return ResponseEntity.ok(service.listByType(type));
    }

    @Operation(summary = "搜索（标题/摘要，已审核）")
    @GetMapping("/search")
    public ResponseEntity<List<CulturalContent>> search(@RequestParam(required = false) String keyword) {
        return ResponseEntity.ok(service.search(keyword));
    }

    @Operation(summary = "按标签筛选（已审核）")
    @GetMapping("/tag")
    public ResponseEntity<List<CulturalContent>> byTag(@RequestParam String tag) {
        return ResponseEntity.ok(service.filterByTag(tag));
    }

    @Operation(summary = "更新文化内容")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody CulturalContent content, BindingResult bindingResult) {
        log.info("接收到更新文化内容请求，ID: {}, 内容: {}", id, content);
        
        // 验证失败处理
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> {
                errors.put(error.getField(), error.getDefaultMessage());
            });
            log.warn("文化内容更新验证失败: {}", errors);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "数据验证失败");
            errorResponse.put("details", errors);
            return ResponseEntity.badRequest().body(errorResponse);
        }
        
        try {
            content.setId(id);
            CulturalContent updated = service.update(content);
            log.info("文化内容更新成功，ID: {}", updated.getId());
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            log.error("更新文化内容时发生错误，ID: " + id, e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "更新失败");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @Operation(summary = "删除文化内容")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "审核通过")
    @PostMapping("/{id}/approve")
    public ResponseEntity<?> approve(@PathVariable Long id, @RequestBody(required = false) Map<String, String> requestBody, HttpServletRequest request) {
        log.info("接收到审核通过请求，ID: {}", id);
        try {
            String comment = requestBody != null ? requestBody.get("comment") : null;
            Long approverId = getCurrentUserId(request);
            
            CulturalContent approved = service.approveContent(id, approverId, comment);
            log.info("文化内容审核通过成功，ID: {}", approved.getId());
            return ResponseEntity.ok(approved);
        } catch (RuntimeException e) {
            log.error("审核通过失败，ID: " + id, e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "审核操作失败");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @Operation(summary = "审核拒绝")
    @PostMapping("/{id}/reject")
    public ResponseEntity<?> reject(@PathVariable Long id, @RequestBody(required = false) Map<String, String> requestBody, HttpServletRequest request) {
        log.info("接收到审核拒绝请求，ID: {}", id);
        try {
            String comment = requestBody != null ? requestBody.get("comment") : null;
            Long approverId = getCurrentUserId(request);
            
            CulturalContent rejected = service.rejectContent(id, approverId, comment);
            log.info("文化内容审核拒绝成功，ID: {}", rejected.getId());
            return ResponseEntity.ok(rejected);
        } catch (RuntimeException e) {
            log.error("审核拒绝失败，ID: " + id, e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "审核操作失败");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @Operation(summary = "增加浏览量")
    @PostMapping("/{id}/view")
    public ResponseEntity<?> increaseViewCount(@PathVariable Long id) {
        log.info("增加文化内容浏览量，ID: {}", id);
        try {
            CulturalContent content = service.increaseViewCount(id);
            Map<String, Object> successResponse = new HashMap<>();
            successResponse.put("message", "浏览量增加成功");
            successResponse.put("viewCount", content.getViewCount());
            return ResponseEntity.ok(successResponse);
        } catch (Exception e) {
            log.error("增加浏览量失败，ID: " + id, e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "增加浏览量失败");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(summary = "增加点赞数")
    @PostMapping("/{id}/like")
    public ResponseEntity<?> increaseLikeCount(@PathVariable Long id) {
        log.info("增加文化内容点赞数，ID: {}", id);
        try {
            CulturalContent content = service.increaseLikeCount(id);
            Map<String, Object> successResponse = new HashMap<>();
            successResponse.put("message", "点赞成功");
            successResponse.put("likeCount", content.getLikeCount());
            return ResponseEntity.ok(successResponse);
        } catch (Exception e) {
            log.error("增加点赞数失败，ID: " + id, e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "点赞失败");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(summary = "获取审核统计信息")
    @GetMapping("/stats")
    public ResponseEntity<?> getApprovalStats() {
        log.info("获取审核统计信息");
        try {
            Map<String, Object> stats = new HashMap<>();
            stats.put("pending", service.countByApprovalStatus(ApprovalStatus.PENDING));
            stats.put("approved", service.countByApprovalStatus(ApprovalStatus.APPROVED));
            stats.put("rejected", service.countByApprovalStatus(ApprovalStatus.REJECTED));
            
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            log.error("获取审核统计信息失败", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "获取统计信息失败");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(summary = "为用户推荐文化内容")
    @GetMapping("/recommend/{userId}")
    public ResponseEntity<?> recommendForUser(@PathVariable Long userId, @RequestParam(defaultValue = "10") int limit) {
        log.info("为用户推荐文化内容，用户ID: {}, 数量: {}", userId, limit);
        try {
            List<CulturalContent> recommendations = recommendationService.recommendForUser(userId, limit);
            return ResponseEntity.ok(recommendations);
        } catch (Exception e) {
            log.error("推荐文化内容失败，用户ID: " + userId, e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "推荐失败");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(summary = "基于内容相似性推荐")
    @GetMapping("/{id}/similar")
    public ResponseEntity<?> recommendSimilar(@PathVariable Long id, @RequestParam(defaultValue = "5") int limit) {
        log.info("基于内容相似性推荐，内容ID: {}, 数量: {}", id, limit);
        try {
            List<CulturalContent> similarContents = recommendationService.recommendSimilar(id, limit);
            return ResponseEntity.ok(similarContents);
        } catch (Exception e) {
            log.error("相似内容推荐失败，内容ID: " + id, e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "推荐失败");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(summary = "记录用户行为")
    @PostMapping("/{id}/action")
    public ResponseEntity<?> recordUserAction(@PathVariable Long id, @RequestBody Map<String, Object> requestBody, HttpServletRequest request) {
        log.info("记录用户行为，内容ID: {}, 行为: {}", id, requestBody.get("action"));
        try {
            String action = (String) requestBody.get("action");
            Long userId = getCurrentUserId(request);
            
            recommendationService.recordUserAction(userId, id, action);
            
            Map<String, Object> successResponse = new HashMap<>();
            successResponse.put("message", "用户行为记录成功");
            return ResponseEntity.ok(successResponse);
        } catch (Exception e) {
            log.error("记录用户行为失败，内容ID: " + id, e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "记录失败");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(summary = "获取推荐统计信息")
    @GetMapping("/recommendation-stats")
    public ResponseEntity<?> getRecommendationStats() {
        log.info("获取推荐统计信息");
        try {
            Map<String, Object> stats = recommendationService.getRecommendationStats();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            log.error("获取推荐统计信息失败", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "获取统计信息失败");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(summary = "高级搜索")
    @PostMapping("/advanced-search")
    public ResponseEntity<?> advancedSearch(@RequestBody Map<String, Object> searchParams) {
        log.info("执行高级搜索，参数: {}", searchParams);
        try {
            List<CulturalContent> results = searchService.advancedSearch(searchParams);
            Map<String, Object> response = new HashMap<>();
            response.put("results", results);
            response.put("total", results.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("高级搜索失败", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "搜索失败");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(summary = "全文搜索")
    @GetMapping("/full-text-search")
    public ResponseEntity<?> fullTextSearch(@RequestParam String query) {
        log.info("执行全文搜索，查询: {}", query);
        try {
            List<CulturalContent> results = searchService.fullTextSearch(query);
            Map<String, Object> response = new HashMap<>();
            response.put("results", results);
            response.put("total", results.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("全文搜索失败", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "搜索失败");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(summary = "获取搜索建议")
    @GetMapping("/search-suggestions")
    public ResponseEntity<?> getSearchSuggestions(@RequestParam String query) {
        log.info("获取搜索建议，查询: {}", query);
        try {
            Map<String, Object> suggestions = searchService.getSearchSuggestions(query);
            return ResponseEntity.ok(suggestions);
        } catch (Exception e) {
            log.error("获取搜索建议失败", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "获取建议失败");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(summary = "获取搜索统计")
    @GetMapping("/search-stats")
    public ResponseEntity<?> getSearchStats() {
        log.info("获取搜索统计信息");
        try {
            Map<String, Object> stats = searchService.getSearchStats();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            log.error("获取搜索统计失败", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "获取统计失败");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // 获取当前用户ID的辅助方法（简化实现）
    private Long getCurrentUserId(HttpServletRequest request) {
        // 这里应该从Session或JWT中获取当前用户ID
        // 为了简化，这里返回一个默认值
        return 1L; // 假设当前用户ID为1（管理员）
    }
}


