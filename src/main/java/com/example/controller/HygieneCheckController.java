package com.example.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.HygieneCheck;
import com.example.service.HygieneCheckService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/hygiene-checks")
@CrossOrigin(origins = "*")
@Tag(name = "卫生检查", description = "商户卫生检查记录管理")
public class HygieneCheckController {

    @Autowired
    private HygieneCheckService service;

    @PostMapping
    public ResponseEntity<HygieneCheck> create(@Valid @RequestBody HygieneCheck h) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(h));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HygieneCheck> update(@PathVariable Long id, @Valid @RequestBody HygieneCheck h) {
        h.setId(id);
        return ResponseEntity.ok(service.update(h));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<HygieneCheck>> page(
            @RequestParam(required = false) Long merchantId,
            @RequestParam(required = false) String result,
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) {
        return ResponseEntity.ok(service.page(merchantId, result, pageNo, pageSize));
    }
}


