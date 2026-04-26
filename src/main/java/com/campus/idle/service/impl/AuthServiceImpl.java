package com.campus.idle.service.impl;

import com.campus.idle.common.ResultCode;
import com.campus.idle.dto.LoginDTO;
import com.campus.idle.dto.RegisterDTO;
import com.campus.idle.entity.SysRole;
import com.campus.idle.entity.SysUser;
import com.campus.idle.exception.BizException;
import com.campus.idle.repository.SysRoleRepository;
import com.campus.idle.repository.SysUserRepository;
import com.campus.idle.security.CustomUserDetails;
import com.campus.idle.service.AuthService;
import com.campus.idle.util.JwtUtil;
import com.campus.idle.vo.LoginVO;
import com.campus.idle.vo.UserVO;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    private final SysUserRepository userRepository;
    private final SysRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(SysUserRepository userRepository,
                           SysRoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager,
                           JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    @Transactional
    public void register(RegisterDTO dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new BizException(ResultCode.CONFLICT, "用户名已存在");
        }

        SysRole userRole = roleRepository.findByRoleCodeAndDeleted("ROLE_USER", 0)
                .orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "默认角色不存在，请先初始化数据"));

        SysUser user = new SysUser();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setNickname(dto.getNickname());
        user.setRealName(dto.getRealName());
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
        user.getRoles().add(userRole);

        userRepository.save(user);
    }

    @Override
    @Transactional
    public LoginVO login(LoginDTO dto, String ip) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            SysUser user = userDetails.getUser();

            user.setLastLoginTime(LocalDateTime.now());
            user.setLastLoginIp(ip);
            userRepository.save(user);

            Set<String> roleCodes = user.getRoles()
                    .stream()
                    .map(SysRole::getRoleCode)
                    .collect(Collectors.toSet());

            String token = jwtUtil.generateToken(user.getId(), user.getUsername(), roleCodes.stream().toList());

            return LoginVO.builder()
                    .token(token)
                    .tokenType("Bearer")
                    .expiresIn(jwtUtil.getExpirationSeconds())
                    .userInfo(toVO(user))
                    .build();
        } catch (BadCredentialsException e) {
            throw new BizException(ResultCode.UNAUTHORIZED, "用户名或密码错误");
        }
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