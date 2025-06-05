package com.takeitfree.itemmanagement.dto;

import com.takeitfree.itemmanagement.models.Status;
import jakarta.validation.constraints.NotBlank;
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
public class StatusDTO implements Serializable {

    private Long id;

    @NotBlank(message = "name is required")
    @Size(min = 3, max = 25, message = "Title must be between 3 and 25 characters")
    private String name;

    public static StatusDTO toDTO(Status status) {
        return StatusDTO.builder()
                .id(status.getId())
                .name(status.getName())
                .build();
    }

    public static Status toEntity(StatusDTO dto) {
        return Status.builder()
                .id(dto.id)
                .name(dto.name)
                .build();
    }

    public static List<StatusDTO> toDTO(List<Status> statuses) {
        return statuses.stream().map(StatusDTO::toDTO).collect(Collectors.toList());
    }

    public static List<Status> toEntity(List<StatusDTO> dtos) {
        return dtos.stream().map(StatusDTO::toEntity).collect(Collectors.toList());
    }
}
