package com.takeitfree.itemmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryIdDTO implements Serializable {
    private Long id;

    public static CategoryIdDTO toIdDTO(CategoryDTO categoryDTO) {
        return CategoryIdDTO.builder()
                .id(categoryDTO.getId())
                .build();
    }

    public static CategoryDTO toDTO(CategoryIdDTO categoryIdDTO) {
        return CategoryDTO.builder()
                .id(categoryIdDTO.getId())
                .build();
    }
}
