package com.takeitfree.itemmanagement.services;

import com.takeitfree.itemmanagement.models.GeocodingResult;

public interface GeocodingService {
    public GeocodingResult geocodePostalCode(String postalCode);
}
