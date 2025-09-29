package com.example.service;

import com.example.entity.CulturalContent;
import com.example.entity.CulturalContent.ApprovalStatus;
import com.example.entity.CulturalRecommendation;
import com.example.repository.CulturalContentRepository;
import com.example.repository.CulturalRecommendationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class CulturalRecommendationService {

    @Autowired
    private CulturalContentRepository contentRepository;

    @Autowired
    private CulturalRecommendationRepository recommendationRepository;

    /**
     * 为用户推荐文化内容
     */
    @Transactional(readOnly = true)
    public List<CulturalContent> recommendForUser(Long userId, int limit) {
        List<CulturalContent> recommendations = new ArrayList<>();
        
        // 1. 热门内容推荐（基于浏览量和点赞数）
        List<CulturalContent> hotContents = getHotContents(limit / 3);
        recommendations.addAll(hotContents);
        
        // 2. 基于用户历史行为的推荐
        List<CulturalContent> userBasedContents = getUserBasedRecommendations(userId, limit / 3);
        recommendations.addAll(userBasedContents);
        
        // 3. 随机推荐
        List<CulturalContent> randomContents = getRandomContents(limit / 3);
        recommendations.addAll(randomContents);
        
        // 去重并限制数量
        return recommendations.stream()
                .distinct()
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * 基于内容相似性推荐
     */
    @Transactional(readOnly = true)
    public List<CulturalContent> recommendSimilar(Long contentId, int limit) {
        Optional<CulturalContent> sourceContent = contentRepository.findById(contentId);
        if (!sourceContent.isPresent()) {
            return Collections.emptyList();
        }
        
        CulturalContent content = sourceContent.get();
        List<CulturalContent> similarContents = new ArrayList<>();
        
        // 基于类型推荐
        List<CulturalContent> typeBased = contentRepository.findByTypeAndApprovedTrue(content.getType())
                .stream()
                .filter(c -> !c.getId().equals(contentId))
                .limit(limit / 2)
                .collect(Collectors.toList());
        similarContents.addAll(typeBased);
        
        // 基于标签推荐
        if (content.getTags() != null && !content.getTags().trim().isEmpty()) {
            String[] tags = content.getTags().split(",");
            for (String tag : tags) {
                List<CulturalContent> tagBased = contentRepository.findByTag(tag.trim())
                        .stream()
                        .filter(c -> !c.getId().equals(contentId))
                        .limit(limit / tags.length)
                        .collect(Collectors.toList());
                similarContents.addAll(tagBased);
            }
        }
        
        return similarContents.stream()
                .distinct()
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * 记录用户行为
     */
    public void recordUserAction(Long userId, Long contentId, String action) {
        CulturalRecommendation recommendation = new CulturalRecommendation();
        recommendation.setUserId(userId);
        recommendation.setContentId(contentId);
        recommendation.setReason("USER_ACTION");
        
        if ("click".equals(action)) {
            recommendation.setClicked(true);
        } else if ("like".equals(action)) {
            recommendation.setLiked(true);
        }
        
        recommendationRepository.save(recommendation);
    }

    /**
     * 获取热门内容
     */
    @Transactional(readOnly = true)
    private List<CulturalContent> getHotContents(int limit) {
        return contentRepository.findByApprovedTrue()
                .stream()
                .sorted((a, b) -> {
                    int scoreA = (a.getViewCount() != null ? a.getViewCount() : 0) + 
                               (a.getLikeCount() != null ? a.getLikeCount() : 0) * 2;
                    int scoreB = (b.getViewCount() != null ? b.getViewCount() : 0) + 
                               (b.getLikeCount() != null ? b.getLikeCount() : 0) * 2;
                    return Integer.compare(scoreB, scoreA);
                })
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * 基于用户行为的推荐
     */
    @Transactional(readOnly = true)
    private List<CulturalContent> getUserBasedRecommendations(Long userId, int limit) {
        // 获取用户历史行为
        List<CulturalRecommendation> userHistory = recommendationRepository.findByUserId(userId);

        if (userHistory.isEmpty()) {
            return getRandomContents(limit);
        }

        // 分析用户偏好
        Map<String, Integer> typePreferences = new HashMap<>();
        Map<String, Integer> tagPreferences = new HashMap<>();

        for (CulturalRecommendation rec : userHistory) {
            Optional<CulturalContent> contentOpt = contentRepository.findById(rec.getContentId());
            if (contentOpt.isPresent()) {
                CulturalContent content = contentOpt.get();

                // 统计类型偏好
                String type = content.getType().name();
                typePreferences.put(type, typePreferences.getOrDefault(type, 0) + 1);

                // 统计标签偏好
                if (content.getTags() != null) {
                    String[] tags = content.getTags().split(",");
                    for (String tag : tags) {
                        tagPreferences.put(tag.trim(), tagPreferences.getOrDefault(tag.trim(), 0) + 1);
                    }
                }
            }
        }

        // 基于偏好推荐
        List<CulturalContent> recommendations = new ArrayList<>();

        // 基于类型偏好
        String favoriteType = typePreferences.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

      /*  if (favoriteType != null) {
            recommendations.addAll(contentRepository.findByTypeAndApprovedTrue(
                    CulturalContent.CulturalType.valueOf(favoriteType))
                    .stream()
                    .limit(limit / 2)
                    .collect(Collectors.toList()));
     */

        // 基于标签偏好
        String favoriteTag = tagPreferences.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        if (favoriteTag != null) {
            recommendations.addAll(contentRepository.findByTag(favoriteTag)
                    .stream()
                    .limit(limit / 2)
                    .collect(Collectors.toList()));
        }

        return recommendations.stream()
                .distinct()
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * 获取随机内容
     */
    @Transactional(readOnly = true)
    private List<CulturalContent> getRandomContents(int limit) {
        List<CulturalContent> allContents = contentRepository.findByApprovedTrue();
        Collections.shuffle(allContents);
        return allContents.stream().limit(limit).collect(Collectors.toList());
    }

    /**
     * 获取推荐统计
     */
    @Transactional(readOnly = true)
    public Map<String, Object> getRecommendationStats() {
        Map<String, Object> stats = new HashMap<>();
        
        long totalRecommendations = recommendationRepository.count();
        long clickedRecommendations = recommendationRepository.countByClickedTrue();
        long likedRecommendations = recommendationRepository.countByLikedTrue();
        
        stats.put("totalRecommendations", totalRecommendations);
        stats.put("clickedRecommendations", clickedRecommendations);
        stats.put("likedRecommendations", likedRecommendations);
        stats.put("clickRate", totalRecommendations > 0 ? (double) clickedRecommendations / totalRecommendations : 0);
        stats.put("likeRate", totalRecommendations > 0 ? (double) likedRecommendations / totalRecommendations : 0);
        
        return stats;
    }
}
