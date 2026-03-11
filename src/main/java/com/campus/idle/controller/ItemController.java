package com.campus.idle.controller;

import com.campus.idle.common.PageResult;
import com.campus.idle.common.Result;
import com.campus.idle.dto.item.ItemCreateDTO;
import com.campus.idle.dto.item.ItemQueryDTO;
import com.campus.idle.dto.item.ItemUpdateDTO;
import com.campus.idle.service.ItemService;
import com.campus.idle.vo.item.ItemDetailVO;
import com.campus.idle.vo.item.ItemSimpleVO;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/items")
@Validated
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public Result<ItemDetailVO> create(@Valid @RequestBody ItemCreateDTO dto) {
        return Result.success(itemService.create(dto));
    }

    @PutMapping("/{id}")
    public Result<ItemDetailVO> update(@PathVariable Long id, @Valid @RequestBody ItemUpdateDTO dto) {
        return Result.success(itemService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        itemService.delete(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<ItemDetailVO> detail(@PathVariable Long id) {
        return Result.success(itemService.detail(id));
    }

    @GetMapping
    public Result<PageResult<ItemSimpleVO>> page(@Valid ItemQueryDTO dto) {
        return Result.success(itemService.page(dto));
    }
}
