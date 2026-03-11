package com.campus.idle.service;

import com.campus.idle.common.PageResult;
import com.campus.idle.dto.item.ItemCreateDTO;
import com.campus.idle.dto.item.ItemQueryDTO;
import com.campus.idle.dto.item.ItemUpdateDTO;
import com.campus.idle.vo.item.ItemDetailVO;
import com.campus.idle.vo.item.ItemSimpleVO;

public interface ItemService {
    ItemDetailVO create(ItemCreateDTO dto);

    ItemDetailVO update(Long id, ItemUpdateDTO dto);

    void delete(Long id);

    ItemDetailVO detail(Long id);

    PageResult<ItemSimpleVO> page(ItemQueryDTO dto);
}
