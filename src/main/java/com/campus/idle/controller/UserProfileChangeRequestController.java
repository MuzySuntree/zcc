package com.campus.idle.controller;

import com.campus.idle.common.PageResult;
import com.campus.idle.common.Result;
import com.campus.idle.dto.UserProfileChangeRequestCreateDTO;
import com.campus.idle.service.UserProfileChangeRequestService;
import com.campus.idle.vo.UserProfileChangeRequestVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserProfileChangeRequestController {

    private final UserProfileChangeRequestService requestService;

    public UserProfileChangeRequestController(UserProfileChangeRequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping("/api/profile-change-requests")
    public Result<Void> submit(@Valid @RequestBody UserProfileChangeRequestCreateDTO dto) {
        requestService.submit(dto);
        return Result.success();
    }

    @GetMapping("/api/profile-change-requests/my")
    public Result<PageResult<UserProfileChangeRequestVO>> my(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success(requestService.myList(pageNum, pageSize));
    }

    @GetMapping("/api/admin/profile-change-requests")
    public Result<PageResult<UserProfileChangeRequestVO>> adminList(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "20") int pageSize) {
        return Result.success(requestService.adminList(pageNum, pageSize));
    }

    @PutMapping("/api/admin/profile-change-requests/{id}/approve")
    public Result<Void> approve(@PathVariable Long id) {
        requestService.approve(id);
        return Result.success();
    }

    @PutMapping("/api/admin/profile-change-requests/{id}/reject")
    public Result<Void> reject(@PathVariable Long id) {
        requestService.reject(id);
        return Result.success();
    }
}