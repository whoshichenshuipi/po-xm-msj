package com.example.service;

import com.example.entity.CulturalTag;
import com.example.repository.CulturalTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CulturalTagService {

    @Autowired
    private CulturalTagRepository repository;

    public CulturalTag create(CulturalTag tag) {
        tag.setId(null);
        tag.setUsageCount(0);
        tag.setEnabled(true);
        return repository.save(tag);
    }

    @Transactional(readOnly = true)
    public Optional<CulturalTag> findById(Long id) {
        return repository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<CulturalTag> findAllEnabled() {
        return repository.findAllEnabledOrderedByUsage();
    }

    @Transactional(readOnly = true)
    public List<CulturalTag> findAll() {
        return repository.findAll();
    }

    //@Transactional(readOnly = true)
    //public List<CulturalTag> findTopUsedTags(int limit) {
      //  return repository.findTopUsedTags(limit);
    //}

    @Transactional(readOnly = true)
    public List<CulturalTag> searchByName(String keyword) {
        return repository.findByNameContaining(keyword);
    }

    public CulturalTag update(CulturalTag tag) {
        if (!repository.existsById(tag.getId())) {
            throw new RuntimeException("标签不存在，ID: " + tag.getId());
        }
        return repository.save(tag);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public CulturalTag toggleEnabled(Long id) {
        CulturalTag tag = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("标签不存在，ID: " + id));
        tag.setEnabled(!tag.getEnabled());
        return repository.save(tag);
    }

    public void updateUsageCount(String tagName) {
        Optional<CulturalTag> tagOpt = repository.findByName(tagName);
        if (tagOpt.isPresent()) {
            CulturalTag tag = tagOpt.get();
            Long count = repository.countContentByTagName(tagName);
            tag.setUsageCount(count.intValue());
            repository.save(tag);
        }
    }

    public void updateAllUsageCounts() {
        List<CulturalTag> tags = repository.findAll();
        for (CulturalTag tag : tags) {
            Long count = repository.countContentByTagName(tag.getName());
            tag.setUsageCount(count.intValue());
            repository.save(tag);
        }
    }

    @Transactional(readOnly = true)
    public Optional<CulturalTag> findByName(String name) {
        return repository.findByName(name);
    }

    public CulturalTag createOrGetTag(String tagName) {
        Optional<CulturalTag> existingTag = repository.findByName(tagName);
        if (existingTag.isPresent()) {
            return existingTag.get();
        } else {
            CulturalTag newTag = new CulturalTag();
            newTag.setName(tagName);
            newTag.setDescription("自动创建的标签");
            newTag.setUsageCount(0);
            newTag.setEnabled(true);
            return repository.save(newTag);
        }
    }
}
