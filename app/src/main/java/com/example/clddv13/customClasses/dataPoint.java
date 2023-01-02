package com.example.clddv13.customClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class dataPoint {
    @SerializedName("long")
    @Expose
    private String longs;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("type")
    @Expose
    private String dataType;

    private Double longitude;
    private Double latitude;

    public dataPoint() {
    }

    public dataPoint(String longs, String lat, String dataType, Double longitude, Double latitude) {
        this.longs = longs;
        this.lat = lat;
        this.dataType = dataType;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getLongs() {
        return longs;
    }

    public void setLongs(String longs) {
        this.longs = longs;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
}
