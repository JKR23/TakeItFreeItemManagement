package com.takeitfree.itemmanagement.dto;

import com.takeitfree.itemmanagement.models.Item;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ItemDTO implements Serializable {
    private Long id;

    @NotBlank(message = "Title is required")
    @Size(min = 2, max = 100, message = "Title must be between 2 and 100 characters")
    private String title;

    @NotBlank(message = "Image is required")
    private String image;

    @NotNull(message = "Category is required")
    private CategoryIdDTO categoryId;

    @NotNull(message = "Status is required")
    private StatusIdDTO statusId;

    @NotBlank(message = "Description is required")
    @Size(min = 10, max = 1000, message = "Description must be between 10 and 1000 characters")
    private String description;

    @NotBlank(message = "Localization is required")
    private String localization;

    @DecimalMin(value = "0.0", message = "Distance must be a positive number")
    private Float distance;

    private boolean taken;


    public static ItemDTO toDTO(Item item) {
        return ItemDTO.builder()
                .id(item.getId())
                .title(item.getTitle())
                .image(item.getImage())
                .categoryId(CategoryIdDTO.toIdDTO(
                        CategoryDTO.toDTO(item.getCategory())
                ))
                .statusId(StatusIdDTO.toIdDTO(
                        StatusDTO.toDTO(item.getStatus())
                ))
                .description(item.getDescription())
                .localization(item.getLocalization())
                .distance(item.getDistance())
                .taken(item.isTaken())
                .build();
    }

    public static Item toEntity(ItemDTO item) {
        return Item.builder()
                .id(item.getId())
                .title(item.getTitle())
                .image(item.getImage())
                .category(CategoryDTO.toEntity(
                        CategoryIdDTO.toDTO(item.categoryId) //entity<-dto<--idDTO
                ))
                .status(StatusDTO.toEntity(
                        StatusIdDTO.toDTO(item.statusId)
                ))
                .description(item.getDescription())
                .localization(item.getLocalization())
                .distance(item.getDistance())
                .taken(item.isTaken())
                .build();
    }

    public static List<ItemDTO> toDTO(List<Item> items) {
        return items.stream().map(ItemDTO::toDTO).collect(Collectors.toList());
    }

    public static List<Item> toEntity(List<ItemDTO> items) {
        return items.stream().map(ItemDTO::toEntity).collect(Collectors.toList());
    }
}
