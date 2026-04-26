package com.campus.idle.controller;

import com.campus.idle.common.Result;
import com.campus.idle.entity.SysUser;
import com.campus.idle.entity.UserPasswordResetRequest;
import com.campus.idle.repository.SysUserRepository;
import com.campus.idle.repository.UserPasswordResetRequestRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/password-reset")
public class UserPasswordResetRequestController {

    private final UserPasswordResetRequestRepository repo;
    private final SysUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserPasswordResetRequestController(UserPasswordResetRequestRepository repo,
                                              SysUserRepository userRepository,
                                              PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 用户提交密码重置申请
    @PostMapping
    public Result<Void> create(@RequestBody UserPasswordResetRequest req) {
        repo.save(req);
        return Result.success();
    }

    // 管理员查看所有申请
    @GetMapping("/admin")
    public Result<List<UserPasswordResetRequest>> list() {
        return Result.success(repo.findAll());
    }

    // 管理员处理申请：把密码重置为 12345678
    @PutMapping("/admin/{id}/reset")
    public Result<Void> reset(@PathVariable Long id) {
        UserPasswordResetRequest req = repo.findById(id).orElseThrow();

        SysUser user = userRepository.findByUsernameAndDeleted(req.getUsername(), 0)
                .orElseThrow();

        user.setPassword(passwordEncoder.encode("12345678"));
        userRepository.save(user);

        req.setStatus(2);
        repo.save(req);

        return Result.success();
    }
}