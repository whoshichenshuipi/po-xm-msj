package com.example.service;

import com.example.entity.CulturalContent;
import com.example.entity.CulturalContent.ApprovalStatus;
import com.example.entity.CulturalType;
import com.example.repository.CulturalContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CulturalContentService {

    @Autowired
    private CulturalContentRepository repository;

    public CulturalContent create(CulturalContent content) {
        content.setId(null);
        content.setApproved(false);
        content.setApprovalStatus(ApprovalStatus.PENDING);
        content.setViewCount(0);
        content.setLikeCount(0);
        return repository.save(content);
    }

    public CulturalContent createWithSubmitter(CulturalContent content, Long submitterId) {
        content.setId(null);
        content.setApproved(false);
        content.setApprovalStatus(ApprovalStatus.PENDING);
        content.setSubmitterId(submitterId);
        content.setViewCount(0);
        content.setLikeCount(0);
        return repository.save(content);
    }

    @Transactional(readOnly = true)
    public Optional<CulturalContent> findById(Long id) {
        return repository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<CulturalContent> listAllApproved() {
        return repository.findByApprovedTrue();
    }

    @Transactional(readOnly = true)
    public List<CulturalContent> listByType(CulturalType type) {
        return repository.findByTypeAndApprovedTrue(type);
    }

    @Transactional(readOnly = true)
    public List<CulturalContent> listAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public List<CulturalContent> listAllPending() {
        return repository.findByApprovedFalse();
    }

    // 新增的审核状态相关方法
    @Transactional(readOnly = true)
    public List<CulturalContent> listByApprovalStatus(ApprovalStatus status) {
        return repository.findByApprovalStatus(status);
    }

    @Transactional(readOnly = true)
    public List<CulturalContent> listByApprovalStatusAndType(ApprovalStatus status, CulturalType type) {
        return repository.findByApprovalStatusAndType(status, type);
    }

    @Transactional(readOnly = true)
    public List<CulturalContent> listBySubmitter(Long submitterId) {
        return repository.findBySubmitterId(submitterId);
    }

    @Transactional(readOnly = true)
    public List<CulturalContent> listByApprover(Long approverId) {
        return repository.findByApproverId(approverId);
    }

    public CulturalContent update(CulturalContent content) {
        if (!repository.existsById(content.getId())) {
            throw new RuntimeException("文化内容不存在，ID: " + content.getId());
        }
        return repository.save(content);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public CulturalContent approve(Long id, boolean approved) {
        CulturalContent content = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("文化内容不存在，ID: " + id));
        content.setApproved(approved);
        return repository.save(content);
    }

    // 新的审核方法
    public CulturalContent approveContent(Long id, Long approverId, String comment) {
        CulturalContent content = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("文化内容不存在，ID: " + id));
        
        content.setApprovalStatus(ApprovalStatus.APPROVED);
        content.setApproved(true);
        content.setApproverId(approverId);
        content.setApprovalTime(LocalDateTime.now());
        content.setApprovalComment(comment);
        
        return repository.save(content);
    }

    public CulturalContent rejectContent(Long id, Long approverId, String comment) {
        CulturalContent content = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("文化内容不存在，ID: " + id));
        
        content.setApprovalStatus(ApprovalStatus.REJECTED);
        content.setApproved(false);
        content.setApproverId(approverId);
        content.setApprovalTime(LocalDateTime.now());
        content.setApprovalComment(comment);
        
        return repository.save(content);
    }

    public CulturalContent increaseViewCount(Long id) {
        CulturalContent content = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("文化内容不存在，ID: " + id));
        
        content.setViewCount(content.getViewCount() + 1);
        return repository.save(content);
    }

    public CulturalContent increaseLikeCount(Long id) {
        CulturalContent content = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("文化内容不存在，ID: " + id));
        
        content.setLikeCount(content.getLikeCount() + 1);
        return repository.save(content);
    }

    @Transactional(readOnly = true)
    public List<CulturalContent> search(String keyword) {
        return repository.searchApproved(keyword);
    }

    @Transactional(readOnly = true)
    public List<CulturalContent> searchByApprovalStatus(ApprovalStatus status, String keyword) {
        return repository.searchByApprovalStatus(status, keyword);
    }

    @Transactional(readOnly = true)
    public List<CulturalContent> filterByTag(String tag) {
        return repository.findByTag(tag);
    }

    @Transactional(readOnly = true)
    public List<CulturalContent> filterByApprovalStatusAndTag(ApprovalStatus status, String tag) {
        return repository.findByApprovalStatusAndTag(status, tag);
    }

    // 统计方法
    @Transactional(readOnly = true)
    public Long countByApprovalStatus(ApprovalStatus status) {
        return repository.countByApprovalStatus(status);
    }

    @Transactional(readOnly = true)
    public Long countBySubmitter(Long submitterId) {
        return repository.countBySubmitterId(submitterId);
    }
}


