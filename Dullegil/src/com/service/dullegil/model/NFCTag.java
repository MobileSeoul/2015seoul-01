package com.service.dullegil.model;

public class NFCTag {
    private int tag_id;
    private double latitude;
    private double longitude;

    public NFCTag(int tag_id, double latitude, double longitude) {
        this.tag_id = tag_id;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getTag_id() {
        return tag_id;
    }

    public void setTag_id(int tag_id) {
        this.tag_id = tag_id;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
