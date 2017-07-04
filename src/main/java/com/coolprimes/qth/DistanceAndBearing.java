package com.coolprimes.qth;

/**
 * Simple container class for distance (km) and bearing (deg)
 */
public class DistanceAndBearing{
    private double distance;
    private double bearing;
    /**
     * Create an instance of the class and validate arguments
     * @param distance is not less than 0 km
     * @param bearing is in [0.0 - 360.0]
     */
    public DistanceAndBearing(double distance, double bearing){
        if(distance < 0.0){
            throw new IllegalArgumentException("Distance cannot be negative");
        }
        if(0.0 > bearing || bearing > 360.0){
            throw new IllegalArgumentException("Bearing must be in [0.0 - 360.0]");
        }
        this.distance = distance;
        this.bearing = bearing;
    }
    /**
     * Return the value of the bearing
     * @return bearing in [0.0 - 360.0]
     */
    public double getBearing(){
        return this.bearing;
    }
    /**
     * Return the distance (in km)
     * @return (a non-negative) distance
     */
    public double getDistance(){
        return this.distance;
    }
}
