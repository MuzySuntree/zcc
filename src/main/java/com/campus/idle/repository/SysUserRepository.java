package com.campus.idle.repository;

import com.campus.idle.entity.SysUser;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SysUserRepository extends JpaRepository<SysUser, Long> {

    @EntityGraph(attributePaths = "roles")
    Optional<SysUser> findByUsernameAndDeleted(String username, Integer deleted);

    boolean existsByUsername(String username);
}
