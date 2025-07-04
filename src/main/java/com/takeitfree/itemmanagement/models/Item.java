package com.takeitfree.itemmanagement.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Item implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @NotBlank(message = "Title is required")
    @Size(min = 2, max = 100, message = "Title must be between 2 and 100 characters")
    private String title;

    @NotBlank(message = "Image is required")
    @Size(max = 300, message = "image path too long")
    private String image;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private Status status;

    @NotBlank(message = "postalCode is required")
    @Size(min = 7, max = 7, message = "invalid postal code")
    private String postalCode;

    private Double latitude;

    private Double longitude;

    @Size(max = 50, message = "city too long")
    private String city;

    private boolean taken;

    private Long userIdPublisher;

}
