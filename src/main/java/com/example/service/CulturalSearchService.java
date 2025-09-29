package com.example.service;

import com.example.entity.CulturalContent;
import com.example.entity.CulturalContent.ApprovalStatus;
import com.example.entity.CulturalType;
import com.example.repository.CulturalContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class CulturalSearchService {

    @Autowired
    private CulturalContentRepository repository;

    /**
     * 高级搜索
     */
    @Transactional(readOnly = true)
    public List<CulturalContent> advancedSearch(Map<String, Object> searchParams) {
        List<CulturalContent> results = new ArrayList<>();
        
        // 获取所有已审核的内容作为基础
        List<CulturalContent> allApproved = repository.findByApprovedTrue();
        
        // 关键词搜索
        String keyword = (String) searchParams.get("keyword");
        if (keyword != null && !keyword.trim().isEmpty()) {
            results = searchByKeyword(allApproved, keyword.trim());
        } else {
            results = allApproved;
        }
        
        // 类型筛选
        String typeStr = (String) searchParams.get("type");
        if (typeStr != null && !typeStr.trim().isEmpty()) {
            try {
                CulturalType type = CulturalType.valueOf(typeStr);
                results = results.stream()
                        .filter(content -> content.getType() == type)
                        .collect(Collectors.toList());
            } catch (IllegalArgumentException e) {
                // 忽略无效的类型
            }
        }
        
        // 标签筛选
        String tag = (String) searchParams.get("tag");
        if (tag != null && !tag.trim().isEmpty()) {
            results = results.stream()
                    .filter(content -> content.getTags() != null && 
                            content.getTags().toLowerCase().contains(tag.toLowerCase()))
                    .collect(Collectors.toList());
        }
        
        // 分类筛选
        Long categoryId = (Long) searchParams.get("categoryId");
        if (categoryId != null) {
            results = results.stream()
                    .filter(content -> Objects.equals(content.getCategoryId(), categoryId))
                    .collect(Collectors.toList());
        }
        
        // 时间范围筛选
        String startDate = (String) searchParams.get("startDate");
        String endDate = (String) searchParams.get("endDate");
        if (startDate != null || endDate != null) {
            results = filterByDateRange(results, startDate, endDate);
        }
        
        // 排序
        String sortBy = (String) searchParams.getOrDefault("sortBy", "createdAt");
        String sortOrder = (String) searchParams.getOrDefault("sortOrder", "desc");
        results = sortResults(results, sortBy, sortOrder);
        
        return results;
    }

    /**
     * 全文搜索
     */
    @Transactional(readOnly = true)
    public List<CulturalContent> fullTextSearch(String query) {
        if (query == null || query.trim().isEmpty()) {
            return Collections.emptyList();
        }
        
        String[] keywords = query.trim().split("\\s+");
        List<CulturalContent> allApproved = repository.findByApprovedTrue();
        
        // 计算每个内容的相关性得分
        Map<CulturalContent, Double> scores = new HashMap<>();
        
        for (CulturalContent content : allApproved) {
            double score = calculateRelevanceScore(content, keywords);
            if (score > 0) {
                scores.put(content, score);
            }
        }
        
        // 按得分排序
        return scores.entrySet().stream()
                .sorted(Map.Entry.<CulturalContent, Double>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    /**
     * 智能搜索建议
     */
    @Transactional(readOnly = true)
    public Map<String, Object> getSearchSuggestions(String query) {
        Map<String, Object> suggestions = new HashMap<>();
        
        if (query == null || query.trim().isEmpty()) {
            return suggestions;
        }
        
        String keyword = query.trim().toLowerCase();
        List<CulturalContent> allApproved = repository.findByApprovedTrue();
        
        // 标题建议
        List<String> titleSuggestions = allApproved.stream()
                .map(CulturalContent::getTitle)
                .filter(title -> title != null && title.toLowerCase().contains(keyword))
                .distinct()
                .limit(5)
                .collect(Collectors.toList());
        
        // 标签建议
        Set<String> tagSuggestions = allApproved.stream()
                .map(CulturalContent::getTags)
                .filter(tags -> tags != null)
                .flatMap(tags -> Arrays.stream(tags.split(",")))
                .map(String::trim)
                .filter(tag -> tag.toLowerCase().contains(keyword))
                .distinct()
                .limit(5)
                .collect(Collectors.toSet());
        
        // 类型建议
        List<String> typeSuggestions = Arrays.stream(CulturalType.values())
                .map(Enum::name)
                .filter(type -> type.toLowerCase().contains(keyword))
                .limit(3)
                .collect(Collectors.toList());
        
        suggestions.put("titles", titleSuggestions);
        suggestions.put("tags", tagSuggestions);
        suggestions.put("types", typeSuggestions);
        
        return suggestions;
    }

    /**
     * 搜索统计
     */
    @Transactional(readOnly = true)
    public Map<String, Object> getSearchStats() {
        Map<String, Object> stats = new HashMap<>();
        
        List<CulturalContent> allApproved = repository.findByApprovedTrue();
        
        // 按类型统计
        Map<CulturalType, Long> typeStats = allApproved.stream()
                .collect(Collectors.groupingBy(CulturalContent::getType, Collectors.counting()));
        
        // 按标签统计
        Map<String, Long> tagStats = allApproved.stream()
                .map(CulturalContent::getTags)
                .filter(tags -> tags != null)
                .flatMap(tags -> Arrays.stream(tags.split(",")))
                .map(String::trim)
                .collect(Collectors.groupingBy(tag -> tag, Collectors.counting()));
        
        // 热门标签（使用次数最多的）
        List<Map.Entry<String, Long>> popularTags = tagStats.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(10)
                .collect(Collectors.toList());
        
        stats.put("totalContent", allApproved.size());
        stats.put("typeStats", typeStats);
        stats.put("popularTags", popularTags);
        
        return stats;
    }

    /**
     * 根据关键词搜索
     */
    private List<CulturalContent> searchByKeyword(List<CulturalContent> contents, String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        
        return contents.stream()
                .filter(content -> {
                    boolean titleMatch = content.getTitle() != null && 
                            content.getTitle().toLowerCase().contains(lowerKeyword);
                    boolean summaryMatch = content.getSummary() != null && 
                            content.getSummary().toLowerCase().contains(lowerKeyword);
                    boolean contentMatch = content.getContent() != null && 
                            content.getContent().toLowerCase().contains(lowerKeyword);
                    boolean tagMatch = content.getTags() != null && 
                            content.getTags().toLowerCase().contains(lowerKeyword);
                    
                    return titleMatch || summaryMatch || contentMatch || tagMatch;
                })
                .collect(Collectors.toList());
    }

    /**
     * 计算相关性得分
     */
    private double calculateRelevanceScore(CulturalContent content, String[] keywords) {
        double score = 0.0;
        
        for (String keyword : keywords) {
            String lowerKeyword = keyword.toLowerCase();
            
            // 标题匹配权重最高
            if (content.getTitle() != null && content.getTitle().toLowerCase().contains(lowerKeyword)) {
                score += 3.0;
            }
            
            // 摘要匹配权重中等
            if (content.getSummary() != null && content.getSummary().toLowerCase().contains(lowerKeyword)) {
                score += 2.0;
            }
            
            // 内容匹配权重较低
            if (content.getContent() != null && content.getContent().toLowerCase().contains(lowerKeyword)) {
                score += 1.0;
            }
            
            // 标签匹配权重中等
            if (content.getTags() != null && content.getTags().toLowerCase().contains(lowerKeyword)) {
                score += 2.0;
            }
        }
        
        return score;
    }

    /**
     * 按时间范围筛选
     */
    private List<CulturalContent> filterByDateRange(List<CulturalContent> contents, String startDate, String endDate) {
        // 这里简化处理，实际应该解析日期字符串
        return contents; // 暂时返回原列表
    }

    /**
     * 排序结果
     */
    private List<CulturalContent> sortResults(List<CulturalContent> contents, String sortBy, String sortOrder) {
        Comparator<CulturalContent> comparator;
        
        switch (sortBy.toLowerCase()) {
            case "title":
                comparator = Comparator.comparing(CulturalContent::getTitle);
                break;
            case "viewcount":
                comparator = Comparator.comparing(content -> content.getViewCount() != null ? content.getViewCount() : 0);
                break;
            case "likecount":
                comparator = Comparator.comparing(content -> content.getLikeCount() != null ? content.getLikeCount() : 0);
                break;
            case "createdat":
            default:
                comparator = Comparator.comparing(CulturalContent::getCreatedAt);
                break;
        }
        
        if ("desc".equalsIgnoreCase(sortOrder)) {
            comparator = comparator.reversed();
        }
        
        return contents.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }
}
