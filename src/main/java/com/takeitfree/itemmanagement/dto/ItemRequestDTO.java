package com.takeitfree.itemmanagement.dto;

import com.takeitfree.itemmanagement.models.Item;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ItemRequestDTO implements Serializable {
    private Long id;

    @NotBlank(message = "Title is required")
    @Size(min = 2, max = 100, message = "Title must be between 2 and 100 characters")
    private String title;

    @NotBlank(message = "Image is required")
    @Size(max = 300, message = "image path too long")
    private String image;

    @NotNull(message = "Category is required")
    private CategoryIdDTO categoryId;

    @NotNull(message = "Status is required")
    private StatusIdDTO statusId;

    @NotBlank(message = "postalCode is required")
    @Size(min = 7, max = 7, message = "invalid postal code")
    private String postalCode;

    @Setter
    private Double latitude;

    @Setter
    private Double longitude;

    @Setter
    @Size(max = 25, message = "city too long")
    private String city;

    public static ItemRequestDTO toDTO(Item item) {
        return ItemRequestDTO.builder()
                .id(item.getId())
                .title(item.getTitle())
                .image(item.getImage())
                .categoryId(CategoryIdDTO.toIdDTO(
                        CategoryDTO.toDTO(item.getCategory())
                ))
                .statusId(StatusIdDTO.toIdDTO(
                        StatusDTO.toDTO(item.getStatus())
                ))
                .postalCode(item.getPostalCode())
                .latitude(item.getLatitude())
                .longitude(item.getLongitude())
                .city(item.getCity())
                .build();
    }

    public static Item toEntity(ItemRequestDTO item) {
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
                .postalCode(item.getPostalCode())
                .longitude(item.getLongitude())
                .latitude(item.getLatitude())
                .city(item.getCity())
                .build();
    }

    public static List<ItemRequestDTO> toDTO(List<Item> items) {
        return items.stream().map(ItemRequestDTO::toDTO).collect(Collectors.toList());
    }

    public static List<Item> toEntity(List<ItemRequestDTO> items) {
        return items.stream().map(ItemRequestDTO::toEntity).collect(Collectors.toList());
    }
}
