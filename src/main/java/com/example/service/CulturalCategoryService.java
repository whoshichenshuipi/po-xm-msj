package com.example.service;

import com.example.entity.CulturalCategory;
import com.example.repository.CulturalCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CulturalCategoryService {

    @Autowired
    private CulturalCategoryRepository repository;

    public CulturalCategory create(CulturalCategory category) {
        category.setId(null);
        category.setContentCount(0);
        category.setEnabled(true);
        return repository.save(category);
    }

    @Transactional(readOnly = true)
    public Optional<CulturalCategory> findById(Long id) {
        return repository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<CulturalCategory> findAllEnabled() {
        return repository.findAllEnabledOrdered();
    }

    @Transactional(readOnly = true)
    public List<CulturalCategory> findAll() {
        return repository.findAll();
    }

    public CulturalCategory update(CulturalCategory category) {
        if (!repository.existsById(category.getId())) {
            throw new RuntimeException("分类不存在，ID: " + category.getId());
        }
        return repository.save(category);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public CulturalCategory toggleEnabled(Long id) {
        CulturalCategory category = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("分类不存在，ID: " + id));
        category.setEnabled(!category.getEnabled());
        return repository.save(category);
    }

    public void updateContentCount(Long categoryId) {
        Long count = repository.countContentByCategoryId(categoryId);
        repository.updateContentCount(categoryId, count.intValue());
    }

    @Transactional(readOnly = true)
    public Optional<CulturalCategory> findByName(String name) {
        return repository.findByName(name);
    }
}
