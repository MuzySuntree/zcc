package com.campus.idle.service;

import com.campus.idle.dto.category.CategoryCreateDTO;
import com.campus.idle.dto.category.CategoryUpdateDTO;
import com.campus.idle.vo.category.CategoryVO;

import java.util.List;

public interface CategoryService {
    CategoryVO create(CategoryCreateDTO dto);

    CategoryVO update(Long id, CategoryUpdateDTO dto);

    void delete(Long id);

    List<CategoryVO> listAll();
}
