package com.campus.idle.service.impl;

import com.campus.idle.common.ResultCode;
import com.campus.idle.dto.category.CategoryCreateDTO;
import com.campus.idle.dto.category.CategoryUpdateDTO;
import com.campus.idle.entity.ItemCategory;
import com.campus.idle.exception.BizException;
import com.campus.idle.repository.ItemCategoryRepository;
import com.campus.idle.service.CategoryService;
import com.campus.idle.vo.category.CategoryVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final ItemCategoryRepository categoryRepository;

    public CategoryServiceImpl(ItemCategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public CategoryVO create(CategoryCreateDTO dto) {
        if (categoryRepository.existsByCategoryNameAndDeleted(dto.getCategoryName(), 0)) {
            throw new BizException(ResultCode.CONFLICT, "分类名称已存在");
        }
        ItemCategory category = new ItemCategory();
        category.setCategoryName(dto.getCategoryName());
        category.setSortNo(dto.getSortNo());
        category.setIcon(dto.getIcon());
        category.setStatus(dto.getStatus() == null ? 1 : dto.getStatus());
        return toVO(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public CategoryVO update(Long id, CategoryUpdateDTO dto) {
        ItemCategory category = getById(id);
        category.setCategoryName(dto.getCategoryName());
        category.setSortNo(dto.getSortNo());
        category.setIcon(dto.getIcon());
        category.setStatus(dto.getStatus());
        return toVO(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        ItemCategory category = getById(id);
        category.setDeleted(1);
        categoryRepository.save(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryVO> listAll() {
        return categoryRepository.findAllByDeletedOrderBySortNoAscIdDesc(0)
                .stream().map(this::toVO).toList();
    }

    private ItemCategory getById(Long id) {
        return categoryRepository.findByIdAndDeleted(id, 0)
                .orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "分类不存在"));
    }

    private CategoryVO toVO(ItemCategory c) {
        return CategoryVO.builder()
                .id(c.getId())
                .categoryName(c.getCategoryName())
                .sortNo(c.getSortNo())
                .icon(c.getIcon())
                .status(c.getStatus())
                .build();
    }
}
