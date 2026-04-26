package com.campus.idle.repository;

import com.campus.idle.entity.UserPasswordResetRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPasswordResetRequestRepository extends JpaRepository<UserPasswordResetRequest, Long> {
}