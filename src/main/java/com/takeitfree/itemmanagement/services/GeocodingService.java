package com.takeitfree.itemmanagement.services;

import com.takeitfree.itemmanagement.dto.LocationData;

public interface GeocodingService {
    public LocationData getLocationFromPostalCode(String postalCode);
}
