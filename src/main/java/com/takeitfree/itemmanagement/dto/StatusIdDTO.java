package com.takeitfree.itemmanagement.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatusIdDTO implements Serializable {
    private Long id;

    public static StatusIdDTO toIdDTO(StatusDTO statusDTO){
        return StatusIdDTO.builder()
                .id(statusDTO.getId())
                .build();
    }

    public static StatusDTO toDTO(StatusIdDTO statusIdDTO){
        return StatusDTO.builder()
                .id(statusIdDTO.getId())
                .build();
    }
}
