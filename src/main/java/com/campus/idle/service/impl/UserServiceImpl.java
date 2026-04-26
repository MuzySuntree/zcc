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
        throw new BizException(ResultCode.FORBIDDEN, "个人资料不能直接修改，请通过资料修改申请提交");
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

    @Override
    @Transactional
    public void adminResetPassword(Long userId, String newPassword) {
        SysUser current = currentUser();

        boolean currentIsAdmin = current.getRoles().stream()
                .anyMatch(role -> "ROLE_ADMIN".equals(role.getRoleCode()));
        if (!currentIsAdmin) {
            throw new BizException(ResultCode.FORBIDDEN, "无权操作");
        }

        SysUser target = userRepository.findById(userId)
                .orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "用户不存在"));

        boolean targetIsAdmin = target.getRoles().stream()
                .anyMatch(role -> "ROLE_ADMIN".equals(role.getRoleCode()));
        if (targetIsAdmin) {
            throw new BizException(ResultCode.FORBIDDEN, "不能修改管理员密码");
        }

        target.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(target);
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