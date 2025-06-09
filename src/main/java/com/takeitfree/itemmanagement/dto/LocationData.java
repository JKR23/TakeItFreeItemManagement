package com.takeitfree.itemmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Builder
@Getter
@AllArgsConstructor
public class LocationData implements Serializable {
    private double latitude;
    private double longitude;
    private String city;
}
