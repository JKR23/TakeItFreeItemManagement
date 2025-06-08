package com.takeitfree.itemmanagement.services;

import com.takeitfree.itemmanagement.dto.CategoryDTO;
import com.takeitfree.itemmanagement.dto.ItemPublicDTO;
import com.takeitfree.itemmanagement.dto.ItemRequestDTO;

import java.util.List;

public interface CategoryService {
    List<CategoryDTO> getAllCategory();
    CategoryDTO getCategoryByName(String name);
    String addCategory(CategoryDTO categoryDTO);
    String updateCategory(CategoryDTO categoryDTO);
    String deleteCategory(Long id);

    List<ItemPublicDTO> getItemsByCategoryName(String name);

    List<ItemPublicDTO> getItemsByCategoryId(Long id);
    List<ItemPublicDTO> getAllCategoryItems();
}
