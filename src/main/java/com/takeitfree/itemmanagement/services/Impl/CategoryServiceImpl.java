package com.takeitfree.itemmanagement.services.Impl;

import com.takeitfree.itemmanagement.dto.CategoryDTO;
import com.takeitfree.itemmanagement.dto.ItemPublicDTO;
import com.takeitfree.itemmanagement.dto.ItemRequestDTO;
import com.takeitfree.itemmanagement.models.Category;
import com.takeitfree.itemmanagement.repositories.CategoryRepository;
import com.takeitfree.itemmanagement.services.CategoryService;
import com.takeitfree.itemmanagement.validators.ObjectValidator;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final ObjectValidator objectValidator;
    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryDTO> getAllCategory() {
        return CategoryDTO.toDTO(categoryRepository.findAll());
    }

    @Override
    public CategoryDTO getCategoryByName(String name) {
        try {
            objectValidator.validate(name);
            Optional<Category> optionalCategory = Optional.ofNullable(categoryRepository.findByName(name));

            if (optionalCategory.isEmpty()) {
                throw new EntityNotFoundException("Category not found");
            }

            return CategoryDTO.toDTO(optionalCategory.get());
        }catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    @Override
    public String addCategory(CategoryDTO categoryDTO) {
        try {
            objectValidator.validate(categoryDTO);

            Optional<Category> optionalCategoryDTO = Optional.ofNullable(categoryRepository.findByName(categoryDTO.getName()));

            if (optionalCategoryDTO.isPresent()) {
                throw new EntityExistsException("Category already exists");
            }

            categoryRepository.save(CategoryDTO.toEntity(categoryDTO));

            return "Category added successfully";

        } catch (EntityExistsException e) {
            throw new EntityExistsException(e.getMessage());
        }
    }

    @Override
    public String updateCategory(CategoryDTO categoryDTO) {
        try {
            objectValidator.validate(categoryDTO);

            Optional<Category> optionalCategoryDTO = categoryRepository.findById(categoryDTO.getId());

            if (optionalCategoryDTO.isEmpty()) {
                throw new EntityNotFoundException("Category doesn't exists");
            }

            optionalCategoryDTO.get().setName(categoryDTO.getName());

            categoryRepository.save(optionalCategoryDTO.get());

            return "Category updated successfully";

        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    @Override
    public String deleteCategory(Long id) {
        try {
            objectValidator.validate(id);

            Optional<Category> optionalCategoryDTO = categoryRepository.findById(id);

            if (optionalCategoryDTO.isEmpty()) {
                throw new EntityNotFoundException("Category doesn't exists");
            }

            categoryRepository.delete(optionalCategoryDTO.get());

            return "Category deleted successfully";

        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    @Override
    public List<ItemPublicDTO> getItemsByCategoryName(String name) {
        try {
            objectValidator.validate(name);

            Optional<Category> optionalCategory = Optional.ofNullable(categoryRepository.findByName(name));

            if (optionalCategory.isEmpty()) {
                throw new EntityNotFoundException("Category doesn't exists");
            }

            return ItemPublicDTO.toDTO(optionalCategory.get().getItemList());
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    @Override
    public List<ItemPublicDTO> getItemsByCategoryId(Long id) {
        try {
            objectValidator.validate(id);
            Optional<Category> optionalCategory = categoryRepository.findById(id);
            if (optionalCategory.isEmpty()) {
                throw new EntityNotFoundException("Category doesn't exists");
            }

            return ItemPublicDTO.toDTO(optionalCategory.get().getItemList());
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    @Override
    public List<ItemPublicDTO> getAllCategoryItems() {

        List<Category> categories = categoryRepository.findAll();

        return categories.stream()
                .flatMap(category -> category.getItemList().stream()) //turn that into a single stream of all category's items.
                .filter(item -> !item.isTaken()) // filter only isTaken is false
                .map(ItemPublicDTO::toDTO)
                .collect(Collectors.toList());

    }
}
