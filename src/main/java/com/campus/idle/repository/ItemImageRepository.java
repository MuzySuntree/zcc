package com.campus.idle.repository;

import com.campus.idle.entity.ItemImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemImageRepository extends JpaRepository<ItemImage, Long> {
    List<ItemImage> findByItemIdOrderBySortNoAscIdAsc(Long itemId);

    void deleteByItemId(Long itemId);
}
