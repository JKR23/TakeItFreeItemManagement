package com.takeitfree.itemmanagement.dto;

import com.takeitfree.itemmanagement.models.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CategoryDTO implements Serializable {

    private Long id;

    @NotBlank(message = "name is required")
    @Size(min = 3, max = 25, message = "Title must be between 3 and 25 characters")
    private String name;

    public static CategoryDTO toDTO(Category category) {
        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public static Category toEntity(CategoryDTO categoryDTO) {
        return Category.builder()
                .id(categoryDTO.id)
                .name(categoryDTO.name)
                .build();
    }

    public static List<CategoryDTO> toDTO(List<Category> categories) {
        return categories.stream().map(CategoryDTO::toDTO).collect(Collectors.toList());
    }

    public static List<Category> toEntity(List<CategoryDTO> categories) {
        return categories.stream().map(CategoryDTO::toEntity).collect(Collectors.toList());
    }
}
