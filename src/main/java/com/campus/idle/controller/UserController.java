package com.campus.idle.controller;

import com.campus.idle.common.Result;
import com.campus.idle.dto.ChangePasswordDTO;
import com.campus.idle.dto.UpdateProfileDTO;
import com.campus.idle.service.UserService;
import com.campus.idle.vo.UserVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/profile")
    public Result<UserVO> updateProfile(@Valid @RequestBody UpdateProfileDTO dto) {
        return Result.success(userService.updateProfile(dto));
    }

    @PutMapping("/password")
    public Result<Void> changePassword(@Valid @RequestBody ChangePasswordDTO dto) {
        userService.changePassword(dto);
        return Result.success();
    }
}
