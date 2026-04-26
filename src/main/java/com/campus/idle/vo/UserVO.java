package com.campus.idle.vo;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class UserVO {
    private Long id;
    private String username;
    private String nickname;
    private String realName;
    private Integer gender;
    private String phone;
    private String email;
    private String avatarUrl;
    private Integer status;
    private Set<String> roles;
}
