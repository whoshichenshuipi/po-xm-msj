package com.example.repository;

import com.example.entity.CulturalContent;
import com.example.entity.CulturalContent.ApprovalStatus;
import com.example.entity.CulturalType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CulturalContentRepository extends JpaRepository<CulturalContent, Long> {

    List<CulturalContent> findByApprovedTrue();

    List<CulturalContent> findByTypeAndApprovedTrue(CulturalType type);

    @Query("SELECT c FROM CulturalContent c WHERE c.approved = true AND (:keyword IS NULL OR c.title LIKE %:keyword% OR c.summary LIKE %:keyword%)")
    List<CulturalContent> searchApproved(@Param("keyword") String keyword);

    @Query("SELECT c FROM CulturalContent c WHERE c.approved = true AND (:tag IS NULL OR c.tags LIKE %:tag%)")
    List<CulturalContent> findByTag(@Param("tag") String tag);

    List<CulturalContent> findByApprovedFalse();

    // 新增的审核状态相关查询方法
    List<CulturalContent> findByApprovalStatus(ApprovalStatus status);

    List<CulturalContent> findByApprovalStatusAndType(ApprovalStatus status, CulturalType type);

    List<CulturalContent> findBySubmitterId(Long submitterId);

    List<CulturalContent> findByApproverId(Long approverId);

    @Query("SELECT c FROM CulturalContent c WHERE c.approvalStatus = :status AND (:keyword IS NULL OR c.title LIKE %:keyword% OR c.summary LIKE %:keyword%)")
    List<CulturalContent> searchByApprovalStatus(@Param("status") ApprovalStatus status, @Param("keyword") String keyword);

    @Query("SELECT c FROM CulturalContent c WHERE c.approvalStatus = :status AND (:tag IS NULL OR c.tags LIKE %:tag%)")
    List<CulturalContent> findByApprovalStatusAndTag(@Param("status") ApprovalStatus status, @Param("tag") String tag);

    // 统计查询
    @Query("SELECT COUNT(c) FROM CulturalContent c WHERE c.approvalStatus = :status")
    Long countByApprovalStatus(@Param("status") ApprovalStatus status);

    @Query("SELECT COUNT(c) FROM CulturalContent c WHERE c.submitterId = :submitterId")
    Long countBySubmitterId(@Param("submitterId") Long submitterId);
}


