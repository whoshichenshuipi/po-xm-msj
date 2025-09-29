package com.example.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.Activity;
import com.example.entity.ActivitySignup;
import com.example.entity.ActivityVerify;
import com.example.service.ActivityService;
import com.example.service.ActivitySignupService;
import com.example.service.ActivityVerifyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/activities")
@CrossOrigin(origins = "*")
@Tag(name = "活动管理", description = "文化活动管理、报名、核销")
public class ActivityController {

    @Autowired
    private ActivityService activityService;
    @Autowired
    private ActivitySignupService signupService;
    @Autowired
    private ActivityVerifyService verifyService;

    // 活动 CRUD
    @PostMapping
    public ResponseEntity<Activity> create(@Valid @RequestBody Activity a) {
        return ResponseEntity.status(HttpStatus.CREATED).body(activityService.create(a));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Activity> update(@PathVariable Long id, @Valid @RequestBody Activity a) {
        a.setId(id);
        return ResponseEntity.ok(activityService.update(a));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        activityService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Activity found = activityService.findById(id);
        return found == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(found);
    }

    @GetMapping
    public ResponseEntity<Page<Activity>> page(
            @RequestParam(required = false) Long merchantId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) {
        return ResponseEntity.ok(activityService.page(merchantId, status, keyword, pageNo, pageSize));
    }

    // 报名
    @PostMapping("/{id}/signup")
    public ResponseEntity<ActivitySignup> signup(@PathVariable Long id, @Valid @RequestBody ActivitySignup s) {
        s.setActivityId(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(signupService.signup(s));
    }

    @PostMapping("/signups/{signupId}/confirm")
    public ResponseEntity<ActivitySignup> confirm(@PathVariable Long signupId) {
        return ResponseEntity.ok(signupService.confirm(signupId));
    }

    @DeleteMapping("/signups/{signupId}")
    public ResponseEntity<Void> cancel(@PathVariable Long signupId) {
        signupService.cancel(signupId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/signups")
    public ResponseEntity<Page<ActivitySignup>> pageSignups(
            @RequestParam(required = false) Long activityId,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) {
        return ResponseEntity.ok(signupService.page(activityId, userId, status, pageNo, pageSize));
    }

    // 核销
    @PostMapping("/signups/{signupId}/verify")
    public ResponseEntity<ActivityVerify> verify(@PathVariable Long signupId, @Valid @RequestBody ActivityVerify v) {
        v.setSignupId(signupId);
        return ResponseEntity.status(HttpStatus.CREATED).body(verifyService.verify(v));
    }

    @GetMapping("/verifies")
    public ResponseEntity<Page<ActivityVerify>> pageVerifies(
            @RequestParam(required = false) Long activityId,
            @RequestParam(required = false) Long signupId,
            @RequestParam(required = false) Long verifierId,
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) {
        return ResponseEntity.ok(verifyService.page(activityId, signupId, verifierId, pageNo, pageSize));
    }
}


