package com.campus.idle.vo;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserProfileChangeRequestVO {
    private Long id;

    private Long userId;
    private String username;
    private String nickname;

    private String oldNickname;
    private String oldRealName;
    private String oldPhone;
    private String oldEmail;

    private String newNickname;
    private String newRealName;
    private String newPhone;
    private String newEmail;

    private String reason;
    private Integer status;

    private Long reviewedBy;
    private LocalDateTime reviewedAt;
    private LocalDateTime createdAt;
}