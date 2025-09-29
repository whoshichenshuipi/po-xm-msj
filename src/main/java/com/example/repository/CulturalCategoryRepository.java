package com.example.repository;

import com.example.entity.CulturalCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CulturalCategoryRepository extends JpaRepository<CulturalCategory, Long> {

    List<CulturalCategory> findByEnabledTrueOrderBySortOrderAsc();

    Optional<CulturalCategory> findByName(String name);

    @Query("SELECT c FROM CulturalCategory c WHERE c.enabled = true ORDER BY c.sortOrder ASC, c.createdAt ASC")
    List<CulturalCategory> findAllEnabledOrdered();

    @Query("SELECT COUNT(cc) FROM CulturalContent cc WHERE cc.categoryId = :categoryId")
    Long countContentByCategoryId(@Param("categoryId") Long categoryId);

    @Query("UPDATE CulturalCategory c SET c.contentCount = :count WHERE c.id = :id")
    void updateContentCount(@Param("id") Long id, @Param("count") Integer count);
}
