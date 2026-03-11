package com.campus.idle.service.impl;

import com.campus.idle.common.ResultCode;
import com.campus.idle.dto.ChangePasswordDTO;
import com.campus.idle.dto.UpdateProfileDTO;
import com.campus.idle.entity.SysRole;
import com.campus.idle.entity.SysUser;
import com.campus.idle.exception.BizException;
import com.campus.idle.repository.SysUserRepository;
import com.campus.idle.security.CustomUserDetails;
import com.campus.idle.service.UserService;
import com.campus.idle.vo.UserVO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final SysUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(SysUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserVO getCurrentUser() {
        return toVO(currentUser());
    }

    @Override
    @Transactional
    public UserVO updateProfile(UpdateProfileDTO dto) {
        SysUser user = currentUser();
        user.setNickname(dto.getNickname());
        user.setRealName(dto.getRealName());
        user.setGender(dto.getGender());
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
        user.setAvatarUrl(dto.getAvatarUrl());
        userRepository.save(user);
        return toVO(user);
    }

    @Override
    @Transactional
    public void changePassword(ChangePasswordDTO dto) {
        SysUser user = currentUser();
        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new BizException(ResultCode.BAD_REQUEST, "旧密码错误");
        }
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
    }

    private SysUser currentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails details)) {
            throw new BizException(ResultCode.UNAUTHORIZED, "未登录");
        }
        return userRepository.findById(details.getUser().getId())
                .orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "用户不存在"));
    }

    private UserVO toVO(SysUser user) {
        return UserVO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .realName(user.getRealName())
                .gender(user.getGender())
                .phone(user.getPhone())
                .email(user.getEmail())
                .avatarUrl(user.getAvatarUrl())
                .status(user.getStatus())
                .roles(user.getRoles().stream().map(SysRole::getRoleCode).collect(Collectors.toSet()))
                .build();
    }
}
