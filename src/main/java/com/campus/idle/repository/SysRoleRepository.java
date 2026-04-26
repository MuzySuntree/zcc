package com.campus.idle.repository;

import com.campus.idle.entity.SysRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SysRoleRepository extends JpaRepository<SysRole, Long> {
    Optional<SysRole> findByRoleCodeAndDeleted(String roleCode, Integer deleted);
}
