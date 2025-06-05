package com.takeitfree.itemmanagement.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @NotBlank(message = "name is required")
    @Size(min = 3, max = 25, message = "name must be between 3 and 25 characters")
    private String name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.MERGE)
    private List<Item> itemList;
}
