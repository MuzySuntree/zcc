package com.campus.idle.repository;

import com.campus.idle.entity.IdleItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface IdleItemRepository extends JpaRepository<IdleItem, Long>, JpaSpecificationExecutor<IdleItem> {
    Optional<IdleItem> findByIdAndDeleted(Long id, Integer deleted);
}
