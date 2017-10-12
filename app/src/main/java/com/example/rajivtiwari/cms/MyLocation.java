package com.example.rajivtiwari.cms;

/**
 * Created by rajivtiwari on 18/9/17.
 */

public class MyLocation {

    double latitude;
    double longitude;

    public MyLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public MyLocation()
    {
       /* this.latitude = 28;
        this.longitude = 78;
        */
    }

    public double getLatitude() {  return latitude; }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
