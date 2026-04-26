package com.campus.idle.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "user_profile_change_request")
public class UserProfileChangeRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private SysUser user;

    @Column(name = "old_nickname", length = 50)
    private String oldNickname;

    @Column(name = "old_real_name", length = 50)
    private String oldRealName;

    @Column(name = "old_phone", length = 30)
    private String oldPhone;

    @Column(name = "old_email", length = 100)
    private String oldEmail;

    @Column(name = "new_nickname", nullable = false, length = 50)
    private String newNickname;

    @Column(name = "new_real_name", nullable = false, length = 50)
    private String newRealName;

    @Column(name = "new_phone", nullable = false, length = 30)
    private String newPhone;

    @Column(name = "new_email", nullable = false, length = 100)
    private String newEmail;

    @Column(length = 255)
    private String reason;

    @Column(nullable = false)
    private Integer status = 1;

    @Column(name = "reviewed_by")
    private Long reviewedBy;

    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}