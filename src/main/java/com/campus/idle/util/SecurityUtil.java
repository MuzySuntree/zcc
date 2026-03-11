package com.campus.idle.util;

import com.campus.idle.common.ResultCode;
import com.campus.idle.entity.SysRole;
import com.campus.idle.entity.SysUser;
import com.campus.idle.exception.BizException;
import com.campus.idle.security.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {

    public SysUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails details)) {
            throw new BizException(ResultCode.UNAUTHORIZED, "未登录");
        }
        return details.getUser();
    }

    public boolean isAdmin(SysUser user) {
        return user.getRoles().stream().map(SysRole::getRoleCode).anyMatch("ROLE_ADMIN"::equals);
    }
}
