package com.smileflower.santa.flag.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GpsInfoRequest {
    private Double latitude;
    private Double longitude;
    private Double altitude;

    @JsonCreator
    public GpsInfoRequest(@JsonProperty("latitude") Double latitude,@JsonProperty("longitude") Double longitude,@JsonProperty("altitude") Double altitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getAltitude() {
        return altitude;
    }
}
