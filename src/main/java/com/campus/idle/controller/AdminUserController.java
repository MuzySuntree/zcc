package com.campus.idle.controller;

import com.campus.idle.common.Result;
import com.campus.idle.entity.SysUser;
import com.campus.idle.repository.SysUserRepository;
import com.campus.idle.repository.SysRoleRepository;
import com.campus.idle.entity.SysRole;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    private final SysUserRepository userRepository;
    private final SysRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminUserController(SysUserRepository userRepository,
                               SysRoleRepository roleRepository,
                               PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 查询所有用户
    @GetMapping
    public Result<List<SysUser>> list() {
        return Result.success(userRepository.findByDeleted(0));
    }

    // 新增用户
    @PostMapping
    public Result<Void> create(@RequestBody SysUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // 默认普通用户角色
        SysRole role = roleRepository.findByRoleCodeAndDeleted("USER", 0)
                .orElseThrow(() -> new RuntimeException("角色不存在"));
        user.getRoles().add(role);

        userRepository.save(user);
        return Result.success();
    }

    // 修改用户
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody SysUser newUser) {
        SysUser user = userRepository.findById(id).orElseThrow();

        user.setNickname(newUser.getNickname());
        user.setRealName(newUser.getRealName());
        user.setPhone(newUser.getPhone());
        user.setEmail(newUser.getEmail());

        userRepository.save(user);
        return Result.success();
    }

    // 删除用户（逻辑删除）
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        SysUser user = userRepository.findById(id).orElseThrow();
        user.setDeleted(1);
        userRepository.save(user);
        return Result.success();
    }
}