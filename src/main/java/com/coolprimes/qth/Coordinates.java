package com.coolprimes.qth;

/**
 * Simple container class for latitude and longitude
 */
public class Coordinates{
    private double latitude;
    private double longitude;
    /**
     * Create an instance of the Coordinate class and validate arguments
     * @param latitude is in [-90.0 - 90.0]
     * @param longitude is in [-180.0 - 180.0]
     */
    public Coordinates(double latitude, double longitude){
        if(latitude < -90.0 || latitude > 90.0){
            throw new IllegalArgumentException("Latitude must be between -90 and 90");
        }
        if(longitude < -180.0 || longitude > 180.0){
            throw new IllegalArgumentException("Longitude must be between -180 and 180");
        }
        this.latitude = latitude;
        this.longitude = longitude;
    }
    /**
     * Getter for the latitude
     * @return the latitude in [-90.0 - 90.0]
     */
    public double getLatitude(){
        return this.latitude;
    }
    /**
     * Getter for the longitude
     * @return the longitude in [-180.0 - 180.0]
     */
    public double getLongitude(){
        return this.longitude;
    }
}