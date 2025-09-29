package com.example.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.MerchantQualification;
import com.example.service.MerchantQualificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/merchant-qualifications")
@CrossOrigin(origins = "*")
@Tag(name = "商户资质", description = "商户文化资质管理")
public class MerchantQualificationController {

    @Autowired
    private MerchantQualificationService service;

    @PostMapping
    public ResponseEntity<MerchantQualification> create(@Valid @RequestBody MerchantQualification q) {
        MerchantQualification created = service.create(q);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MerchantQualification> update(@PathVariable Long id, @Valid @RequestBody MerchantQualification q) {
        q.setId(id);
        return ResponseEntity.ok(service.update(q));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        MerchantQualification found = service.findById(id);
        return found == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(found);
    }

    @GetMapping
    public ResponseEntity<Page<MerchantQualification>> page(
            @RequestParam(required = false) Long merchantId,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) {
        return ResponseEntity.ok(service.page(merchantId, status, pageNo, pageSize));
    }
}


