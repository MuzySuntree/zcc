package com.campus.idle.controller;

import com.campus.idle.common.Result;
import com.campus.idle.dto.LoginDTO;
import com.campus.idle.dto.RegisterDTO;
import com.campus.idle.service.AuthService;
import com.campus.idle.service.UserService;
import com.campus.idle.vo.LoginVO;
import com.campus.idle.vo.UserVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public Result<Void> register(@Valid @RequestBody RegisterDTO dto) {
        authService.register(dto);
        return Result.success();
    }

    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody LoginDTO dto, HttpServletRequest request) {
        return Result.success(authService.login(dto, request.getRemoteAddr()));
    }

    @GetMapping("/me")
    public Result<UserVO> me() {
        return Result.success(userService.getCurrentUser());
    }
}
