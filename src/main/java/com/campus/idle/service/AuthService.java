package com.campus.idle.service;

import com.campus.idle.dto.LoginDTO;
import com.campus.idle.dto.RegisterDTO;
import com.campus.idle.vo.LoginVO;

public interface AuthService {
    void register(RegisterDTO dto);

    LoginVO login(LoginDTO dto, String ip);
}
