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
public class ItemPublicDTO implements Serializable {
    private Long id;

    @NotBlank(message = "Title is required")
    @Size(min = 2, max = 100, message = "Title must be between 2 and 100 characters")
    private String title;

    @NotBlank(message = "Image is required")
    @Size(max = 300, message = "image path too long")
    private String image;

    @NotNull(message = "Status is required")
    private StatusIdDTO statusId;

    @NotBlank(message = "postalCode is required")
    @Size(min = 7, max = 7, message = "invalid postal code")
    private String postalCode;

    @Setter
    @Size(max = 25, message = "city too long")
    private String city;

    public static ItemPublicDTO toDTO(Item item) {
        return ItemPublicDTO.builder()
                .id(item.getId())
                .title(item.getTitle())
                .image(item.getImage())
                .statusId(StatusIdDTO.toIdDTO(
                        StatusDTO.toDTO(item.getStatus())
                ))
                .postalCode(item.getPostalCode())
                .city(item.getCity())
                .build();
    }

    public static Item toEntity(ItemPublicDTO item) {
        return Item.builder()
                .id(item.getId())
                .title(item.getTitle())
                .image(item.getImage())
                .status(StatusDTO.toEntity(
                        StatusIdDTO.toDTO(item.statusId)
                ))
                .postalCode(item.getPostalCode())
                .city(item.getCity())
                .build();
    }

    public static List<ItemPublicDTO> toDTO(List<Item> items) {
        return items.stream().map(ItemPublicDTO::toDTO).collect(Collectors.toList());
    }

    public static List<Item> toEntity(List<ItemPublicDTO> items) {
        return items.stream().map(ItemPublicDTO::toEntity).collect(Collectors.toList());
    }
}
