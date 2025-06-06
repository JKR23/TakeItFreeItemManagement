package com.takeitfree.itemmanagement.services;

import com.takeitfree.itemmanagement.dto.CategoryDTO;
import com.takeitfree.itemmanagement.dto.ItemDTO;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<CategoryDTO> getAllCategory();
    CategoryDTO getCategoryByName(String name);
    String addCategory(CategoryDTO categoryDTO);
    String updateCategory(CategoryDTO categoryDTO);
    String deleteCategory(Long id);

    List<ItemDTO> getItemsByCategoryName(String name);

    List<ItemDTO> getItemsByCategoryId(Long id);
}
