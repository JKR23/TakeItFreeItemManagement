package com.takeitfree.itemmanagement.services.Impl;

import com.takeitfree.itemmanagement.models.GeocodingResult;
import com.takeitfree.itemmanagement.services.GeocodingService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

@Service
public class GeocodingServiceImpl implements GeocodingService {

    @Value("${google.api.key}")
    private String googleApiKey;

    @Override
    public GeocodingResult geocodePostalCode(String postalCode) {
        String url = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("maps.googleapis.com")
                .path("/maps/api/geocode/json")
                .queryParam("address", postalCode)
                .queryParam("region", "ca")
                .queryParam("key", googleApiKey)
                .build()
                .toUriString();

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);
        JSONObject json = new JSONObject(response);

        JSONArray results = json.getJSONArray("results");
        if (results.isEmpty()) throw new RuntimeException("Code postal introuvable");

        JSONObject location = results.getJSONObject(0)
                .getJSONObject("geometry")
                .getJSONObject("location");

        String city = "";
        JSONArray components = results.getJSONObject(0).getJSONArray("address_components");
        for (int i = 0; i < components.length(); i++) {
            JSONObject comp = components.getJSONObject(i);
            JSONArray types = comp.getJSONArray("types");
            for (int j = 0; j < types.length(); j++) {
                if ("locality".equals(types.getString(j))) {
                    city = comp.getString("long_name");
                    break;
                }
            }
        }

        return new GeocodingResult(location.getDouble("lat"), location.getDouble("lng"), city);
    }
}
