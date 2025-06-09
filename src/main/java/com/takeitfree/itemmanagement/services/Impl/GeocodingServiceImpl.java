package com.takeitfree.itemmanagement.services.Impl;

import com.takeitfree.itemmanagement.dto.GeoLocationIQResponse;
import com.takeitfree.itemmanagement.dto.LocationData;
import com.takeitfree.itemmanagement.services.GeocodingService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class GeocodingServiceImpl implements GeocodingService {

    @Value("${locationiq.api.key}")
    private String locationIqApiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public LocationData getLocationFromPostalCode(String postalCode) {
        if (postalCode == null || postalCode.trim().isEmpty()) {
            throw new RuntimeException("postal code is null or empty");
        }

        URI uri = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("us1.locationiq.com")
                .path("/v1/search")
                .queryParam("key", locationIqApiKey)
                .queryParam("q", URLEncoder.encode(postalCode.trim(), StandardCharsets.UTF_8))
                .queryParam("format", "json")
                .queryParam("addressdetails", "1")
                .build()
                .toUri();

        GeoLocationIQResponse[] results = restTemplate.getForObject(uri, GeoLocationIQResponse[].class);

        if (results == null || results.length == 0) {
            throw new RuntimeException("none result found the postal code");
        }

        GeoLocationIQResponse firstResult = results[0];

        if (firstResult.getLatitude() == null || firstResult.getLongitude() == null) {
            throw new RuntimeException("Latitude or longitude missing");
        }

        String city = null;
        if (firstResult.getAddress() != null) {
            city = firstResult.getAddress().getCity();
            if (city == null) city = firstResult.getAddress().getTown();
            if (city == null) city = firstResult.getAddress().getVillage();
        }

        return LocationData.builder()
                .latitude(Double.parseDouble(firstResult.getLatitude()))
                .longitude(Double.parseDouble(firstResult.getLongitude()))
                .city(city != null ? city : "Unknown city")
                .build();
    }

}
