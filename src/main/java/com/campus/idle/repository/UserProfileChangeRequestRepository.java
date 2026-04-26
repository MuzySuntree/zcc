package com.campus.idle.repository;

import com.campus.idle.entity.UserProfileChangeRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserProfileChangeRequestRepository extends JpaRepository<UserProfileChangeRequest, Long>, JpaSpecificationExecutor<UserProfileChangeRequest> {
}