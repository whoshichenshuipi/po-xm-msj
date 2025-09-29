package com.example.service;

import com.example.entity.CulturalContent;
import com.example.entity.CulturalContent.ApprovalStatus;
import com.example.entity.CulturalType;
import com.example.repository.CulturalContentRepository;
import com.example.repository.CulturalRecommendationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class CulturalStatisticsService {

    @Autowired
    private CulturalContentRepository contentRepository;

    @Autowired
    private CulturalRecommendationRepository recommendationRepository;

    /**
     * 获取总体统计信息
     */
    @Transactional(readOnly = true)
    public Map<String, Object> getOverallStats() {
        Map<String, Object> stats = new HashMap<>();
        
        List<CulturalContent> allContents = contentRepository.findAll();
        List<CulturalContent> approvedContents = contentRepository.findByApprovedTrue();
        
        // 基本统计
        stats.put("totalContent", allContents.size());
        stats.put("approvedContent", approvedContents.size());
        stats.put("pendingContent", contentRepository.countByApprovalStatus(ApprovalStatus.PENDING));
        stats.put("rejectedContent", contentRepository.countByApprovalStatus(ApprovalStatus.REJECTED));
        
        // 总浏览量和点赞数
        int totalViews = approvedContents.stream()
                .mapToInt(content -> content.getViewCount() != null ? content.getViewCount() : 0)
                .sum();
        int totalLikes = approvedContents.stream()
                .mapToInt(content -> content.getLikeCount() != null ? content.getLikeCount() : 0)
                .sum();
        
        stats.put("totalViews", totalViews);
        stats.put("totalLikes", totalLikes);
        
        // 平均数据
        if (!approvedContents.isEmpty()) {
            stats.put("avgViewsPerContent", (double) totalViews / approvedContents.size());
            stats.put("avgLikesPerContent", (double) totalLikes / approvedContents.size());
        } else {
            stats.put("avgViewsPerContent", 0.0);
            stats.put("avgLikesPerContent", 0.0);
        }
        
        return stats;
    }

    /**
     * 获取按类型统计
     */
    @Transactional(readOnly = true)
    public Map<String, Object> getTypeStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        List<CulturalContent> approvedContents = contentRepository.findByApprovedTrue();
        
        // 按类型分组统计
        Map<CulturalType, List<CulturalContent>> typeGroups = approvedContents.stream()
                .collect(Collectors.groupingBy(CulturalContent::getType));
        
        Map<String, Object> typeStats = new HashMap<>();
        for (Map.Entry<CulturalType, List<CulturalContent>> entry : typeGroups.entrySet()) {
            CulturalType type = entry.getKey();
            List<CulturalContent> contents = entry.getValue();
            
            Map<String, Object> typeData = new HashMap<>();
            typeData.put("count", contents.size());
            
            int totalViews = contents.stream()
                    .mapToInt(content -> content.getViewCount() != null ? content.getViewCount() : 0)
                    .sum();
            int totalLikes = contents.stream()
                    .mapToInt(content -> content.getLikeCount() != null ? content.getLikeCount() : 0)
                    .sum();
            
            typeData.put("totalViews", totalViews);
            typeData.put("totalLikes", totalLikes);
            typeData.put("avgViews", contents.isEmpty() ? 0.0 : (double) totalViews / contents.size());
            typeData.put("avgLikes", contents.isEmpty() ? 0.0 : (double) totalLikes / contents.size());
            
            typeStats.put(type.name(), typeData);
        }
        
        stats.put("typeStatistics", typeStats);
        return stats;
    }

    /**
     * 获取时间趋势统计
     */
    @Transactional(readOnly = true)
    public Map<String, Object> getTrendStatistics(String period) {
        Map<String, Object> stats = new HashMap<>();
        
        List<CulturalContent> allContents = contentRepository.findAll();
        LocalDateTime now = LocalDateTime.now();
        
        Map<String, Integer> trendData = new HashMap<>();
        
        switch (period.toLowerCase()) {
            case "daily":
                trendData = getDailyTrend(allContents, now);
                break;
            case "weekly":
                trendData = getWeeklyTrend(allContents, now);
                break;
            case "monthly":
                trendData = getMonthlyTrend(allContents, now);
                break;
            default:
                trendData = getMonthlyTrend(allContents, now);
        }
        
        stats.put("trendData", trendData);
        stats.put("period", period);
        return stats;
    }

    /**
     * 获取热门内容统计
     */
    @Transactional(readOnly = true)
    public Map<String, Object> getPopularContentStats(int limit) {
        Map<String, Object> stats = new HashMap<>();
        
        List<CulturalContent> approvedContents = contentRepository.findByApprovedTrue();
        
        // 按浏览量排序
        List<Map<String, Object>> mostViewed = approvedContents.stream()
                .sorted((a, b) -> Integer.compare(
                        b.getViewCount() != null ? b.getViewCount() : 0,
                        a.getViewCount() != null ? a.getViewCount() : 0))
                .limit(limit)
                .map(this::contentToMap)
                .collect(Collectors.toList());
        
        // 按点赞数排序
        List<Map<String, Object>> mostLiked = approvedContents.stream()
                .sorted((a, b) -> Integer.compare(
                        b.getLikeCount() != null ? b.getLikeCount() : 0,
                        a.getLikeCount() != null ? a.getLikeCount() : 0))
                .limit(limit)
                .map(this::contentToMap)
                .collect(Collectors.toList());
        
        // 综合评分排序（浏览量 + 点赞数*2）
        List<Map<String, Object>> mostPopular = approvedContents.stream()
                .sorted((a, b) -> {
                    int scoreA = (a.getViewCount() != null ? a.getViewCount() : 0) + 
                               (a.getLikeCount() != null ? a.getLikeCount() : 0) * 2;
                    int scoreB = (b.getViewCount() != null ? b.getViewCount() : 0) + 
                               (b.getLikeCount() != null ? b.getLikeCount() : 0) * 2;
                    return Integer.compare(scoreB, scoreA);
                })
                .limit(limit)
                .map(this::contentToMap)
                .collect(Collectors.toList());
        
        stats.put("mostViewed", mostViewed);
        stats.put("mostLiked", mostLiked);
        stats.put("mostPopular", mostPopular);
        
        return stats;
    }

    /**
     * 获取用户行为统计
     */
    @Transactional(readOnly = true)
    public Map<String, Object> getUserBehaviorStats() {
        Map<String, Object> stats = new HashMap<>();
        
        long totalRecommendations = recommendationRepository.count();
        long clickedRecommendations = recommendationRepository.countByClickedTrue();
        long likedRecommendations = recommendationRepository.countByLikedTrue();
        
        stats.put("totalRecommendations", totalRecommendations);
        stats.put("clickedRecommendations", clickedRecommendations);
        stats.put("likedRecommendations", likedRecommendations);
        
        if (totalRecommendations > 0) {
            stats.put("clickRate", (double) clickedRecommendations / totalRecommendations);
            stats.put("likeRate", (double) likedRecommendations / totalRecommendations);
        } else {
            stats.put("clickRate", 0.0);
            stats.put("likeRate", 0.0);
        }
        
        // 推荐原因统计
        List<Object[]> reasonStats = recommendationRepository.getRecommendationReasonStats();
        Map<String, Long> reasonMap = new HashMap<>();
        for (Object[] row : reasonStats) {
            reasonMap.put((String) row[0], (Long) row[1]);
        }
        stats.put("recommendationReasons", reasonMap);
        
        return stats;
    }

    /**
     * 获取标签统计
     */
    @Transactional(readOnly = true)
    public Map<String, Object> getTagStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        List<CulturalContent> approvedContents = contentRepository.findByApprovedTrue();
        
        // 统计标签使用情况
        Map<String, Long> tagUsage = approvedContents.stream()
                .map(CulturalContent::getTags)
                .filter(tags -> tags != null)
                .flatMap(tags -> Arrays.stream(tags.split(",")))
                .map(String::trim)
                .collect(Collectors.groupingBy(tag -> tag, Collectors.counting()));
        
        // 热门标签
        List<Map.Entry<String, Long>> popularTags = tagUsage.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(20)
                .collect(Collectors.toList());
        
        stats.put("totalTags", tagUsage.size());
        stats.put("popularTags", popularTags);
        
        return stats;
    }

    /**
     * 内容转Map
     */
    private Map<String, Object> contentToMap(CulturalContent content) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", content.getId());
        map.put("title", content.getTitle());
        map.put("type", content.getType());
        map.put("viewCount", content.getViewCount() != null ? content.getViewCount() : 0);
        map.put("likeCount", content.getLikeCount() != null ? content.getLikeCount() : 0);
        map.put("createdAt", content.getCreatedAt());
        return map;
    }

    /**
     * 获取日趋势
     */
    private Map<String, Integer> getDailyTrend(List<CulturalContent> contents, LocalDateTime now) {
        Map<String, Integer> trend = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        for (int i = 6; i >= 0; i--) {
            LocalDateTime date = now.minusDays(i);
            String dateStr = date.format(formatter);
            
            int count = (int) contents.stream()
                    .filter(content -> content.getCreatedAt() != null && 
                            content.getCreatedAt().toLocalDate().equals(date.toLocalDate()))
                    .count();
            
            trend.put(dateStr, count);
        }
        
        return trend;
    }

    /**
     * 获取周趋势
     */
    private Map<String, Integer> getWeeklyTrend(List<CulturalContent> contents, LocalDateTime now) {
        Map<String, Integer> trend = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        for (int i = 3; i >= 0; i--) {
            LocalDateTime weekStart = now.minusWeeks(i).with(java.time.DayOfWeek.MONDAY);
            String weekStr = weekStart.format(formatter);
            
            int count = (int) contents.stream()
                    .filter(content -> content.getCreatedAt() != null && 
                            content.getCreatedAt().isAfter(weekStart.minusDays(1)) &&
                            content.getCreatedAt().isBefore(weekStart.plusWeeks(1)))
                    .count();
            
            trend.put(weekStr, count);
        }
        
        return trend;
    }

    /**
     * 获取月趋势
     */
    private Map<String, Integer> getMonthlyTrend(List<CulturalContent> contents, LocalDateTime now) {
        Map<String, Integer> trend = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        
        for (int i = 11; i >= 0; i--) {
            LocalDateTime month = now.minusMonths(i);
            String monthStr = month.format(formatter);
            
            int count = (int) contents.stream()
                    .filter(content -> content.getCreatedAt() != null && 
                            content.getCreatedAt().getYear() == month.getYear() &&
                            content.getCreatedAt().getMonth() == month.getMonth())
                    .count();
            
            trend.put(monthStr, count);
        }
        
        return trend;
    }
}
