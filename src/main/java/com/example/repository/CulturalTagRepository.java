package com.example.repository;

import com.example.entity.CulturalTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CulturalTagRepository extends JpaRepository<CulturalTag, Long> {

    List<CulturalTag> findByEnabledTrueOrderByUsageCountDescSortOrderAsc();

    Optional<CulturalTag> findByName(String name);

    @Query("SELECT t FROM CulturalTag t WHERE t.enabled = true ORDER BY t.usageCount DESC, t.sortOrder ASC")
    List<CulturalTag> findAllEnabledOrderedByUsage();

    @Query("SELECT t FROM CulturalTag t WHERE t.enabled = true AND t.name LIKE %:keyword% ORDER BY t.usageCount DESC")
    List<CulturalTag> findByNameContaining(@Param("keyword") String keyword);

    @Query("SELECT COUNT(cc) FROM CulturalContent cc WHERE cc.tags LIKE %:tagName%")
    Long countContentByTagName(@Param("tagName") String tagName);

    @Query("UPDATE CulturalTag t SET t.usageCount = :count WHERE t.id = :id")
    void updateUsageCount(@Param("id") Long id, @Param("count") Integer count);

    //@Query("SELECT t FROM CulturalTag t WHERE t.enabled = true ORDER BY t.usageCount DESC LIMIT :limit")
    //List<CulturalTag> findTopUsedTags(@Param("limit") int limit);
}
