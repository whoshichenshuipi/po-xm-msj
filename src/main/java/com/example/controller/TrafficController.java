package com.example.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.TrafficStat;
import com.example.service.TrafficService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/traffic")
@CrossOrigin(origins = "*")
@Tag(name = "客流统计", description = "食品街客流数据统计")
public class TrafficController {

    @Autowired
    private TrafficService service;

    @PostMapping
    public ResponseEntity<TrafficStat> create(@RequestBody TrafficStat t) {
        return ResponseEntity.ok(service.create(t));
    }

    @GetMapping
    public ResponseEntity<Page<TrafficStat>> page(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
            @RequestParam(required = false) Long venueId,
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) {
        return ResponseEntity.ok(service.page(start, end, venueId, pageNo, pageSize));
    }
}


