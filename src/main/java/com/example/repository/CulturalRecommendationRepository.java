package com.example.repository;

import com.example.entity.CulturalRecommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CulturalRecommendationRepository extends JpaRepository<CulturalRecommendation, Long> {

    List<CulturalRecommendation> findByUserId(Long userId);

    List<CulturalRecommendation> findByUserIdOrderByScoreDesc(Long userId);

    List<CulturalRecommendation> findByContentId(Long contentId);

    @Query("SELECT r FROM CulturalRecommendation r WHERE r.userId = :userId AND r.clicked = true")
    List<CulturalRecommendation> findByUserIdAndClickedTrue(@Param("userId") Long userId);

    @Query("SELECT r FROM CulturalRecommendation r WHERE r.userId = :userId AND r.liked = true")
    List<CulturalRecommendation> findByUserIdAndLikedTrue(@Param("userId") Long userId);

    @Query("SELECT COUNT(r) FROM CulturalRecommendation r WHERE r.clicked = true")
    Long countByClickedTrue();

    @Query("SELECT COUNT(r) FROM CulturalRecommendation r WHERE r.liked = true")
    Long countByLikedTrue();

    @Query("SELECT r.contentId, COUNT(r) as clickCount FROM CulturalRecommendation r WHERE r.clicked = true GROUP BY r.contentId ORDER BY clickCount DESC")
    List<Object[]> findMostClickedContents();

    @Query("SELECT r.contentId, COUNT(r) as likeCount FROM CulturalRecommendation r WHERE r.liked = true GROUP BY r.contentId ORDER BY likeCount DESC")
    List<Object[]> findMostLikedContents();

    @Query("SELECT r.reason, COUNT(r) FROM CulturalRecommendation r GROUP BY r.reason")
    List<Object[]> getRecommendationReasonStats();
}
