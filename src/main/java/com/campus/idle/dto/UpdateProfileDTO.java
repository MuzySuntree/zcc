package com.campus.idle.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateProfileDTO {
    @NotBlank
    @Size(max = 50)
    private String nickname;

    @Size(max = 50)
    private String realName;

    private Integer gender;

    @Pattern(regexp = "^$|^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    @Email
    private String email;

    @Size(max = 255)
    private String avatarUrl;
}
