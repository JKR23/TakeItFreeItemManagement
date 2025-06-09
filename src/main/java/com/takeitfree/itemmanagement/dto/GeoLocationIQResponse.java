package com.takeitfree.itemmanagement.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeoLocationIQResponse implements Serializable {

    @JsonProperty("lat")
    private String latitude;

    @JsonProperty("lon")
    private String longitude;

    public Address address;

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Address {

        private String postcode;
        public String city;
        public String town;
        public String village;
    }
}
