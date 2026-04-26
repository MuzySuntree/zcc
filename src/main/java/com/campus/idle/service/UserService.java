package com.campus.idle.service;

import com.campus.idle.dto.ChangePasswordDTO;
import com.campus.idle.dto.UpdateProfileDTO;
import com.campus.idle.vo.UserVO;

public interface UserService {
    UserVO getCurrentUser();

    UserVO updateProfile(UpdateProfileDTO dto);

    void changePassword(ChangePasswordDTO dto);

    void adminResetPassword(Long userId, String newPassword);
}