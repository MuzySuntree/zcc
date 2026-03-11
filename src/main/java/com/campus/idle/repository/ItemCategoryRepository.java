package com.campus.idle.repository;

import com.campus.idle.entity.ItemCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemCategoryRepository extends JpaRepository<ItemCategory, Long> {
    List<ItemCategory> findAllByDeletedOrderBySortNoAscIdDesc(Integer deleted);

    Optional<ItemCategory> findByIdAndDeleted(Long id, Integer deleted);

    boolean existsByCategoryNameAndDeleted(String categoryName, Integer deleted);
}
