package com.takeitfree.itemmanagement.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GeocodingResult implements Serializable {
    private double latitude;
    private double longitude;
    private String city;
}
