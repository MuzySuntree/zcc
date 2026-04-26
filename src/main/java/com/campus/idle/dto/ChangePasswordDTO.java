package com.campus.idle.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePasswordDTO {
    @NotBlank
    private String oldPassword;

    @NotBlank
    @Size(min = 8, max = 32)
    private String newPassword;
}
