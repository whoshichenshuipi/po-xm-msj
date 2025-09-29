package com.example.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.Venue;
import com.example.service.VenueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/venues")
@CrossOrigin(origins = "*")
@Tag(name = "场地管理", description = "食品街场地管理、使用状态")
public class VenueController {

    @Autowired
    private VenueService service;

    @PostMapping
    public ResponseEntity<Venue> create(@Valid @RequestBody Venue v) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(v));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Venue> update(@PathVariable Long id, @Valid @RequestBody Venue v) {
        v.setId(id);
        return ResponseEntity.ok(service.update(v));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<Venue>> page(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) {
        return ResponseEntity.ok(service.page(type, status, keyword, pageNo, pageSize));
    }
}


