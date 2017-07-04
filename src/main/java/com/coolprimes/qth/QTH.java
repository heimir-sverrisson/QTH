package com.coolprimes.qth;
/**
 * Conversion and calculation class for location on earth
 */
public class QTH {
    private static final int valueUpperA = (int)'A';
    private static final int value0 = (int)'0';
    private static final int value9 = (int)'9';
    private static final int valueA = (int)'a';
    private static final int valueR = (int)'r';
    private static final int valueX = (int)'x';

    private static double toRadians(double deg){
        return Math.PI * deg / 180.0;
    }

    private static double toDegrees(double rad){
        return 180.0 * rad / Math.PI;
    }

    private static void validateSquare(String square){
        int squareLength = square.length();
        if(squareLength != 4 && squareLength != 6){
            throw new IllegalArgumentException("Square can only be 4 or 6 characters");
        }
        int c = (int)square.charAt(0);
        if(valueA > c || c > valueR){
            throw new IllegalArgumentException("First character must be in [A-R]");
        }
        c = (int)square.charAt(1);
        if(valueA > c || c > valueR){
            throw new IllegalArgumentException("Second character must be in [A-R]");
        }
        c = (int)square.charAt(2);
        if(value0 > c || c > value9){
            throw new IllegalArgumentException("Third character must be in [0-9]");
        }
        c = (int)square.charAt(3);
        if(value0 > c || c > value9){
            throw new IllegalArgumentException("Fourth character must be in [0-9]");
        }
        if(squareLength == 6){
            c = (int)square.charAt(4);
            if(valueA > c || c > valueX){
                throw new IllegalArgumentException("Fifth character must be in [A-X]");
            }
            c = (int)square.charAt(5);
            if(valueA > c || c > valueX){
                throw new IllegalArgumentException("Sixth character must be in [A-X]");
            }
        }
    }

    /**
     * Return the coordinates (lat/long) of the midpoint of a Maidenhead square
     * @param square is a four or six character Maidenhead square string
     * @return the coordinates of the midpoint of the square
     */
    public static Coordinates midpoint(String square){
        square = square.toLowerCase();
        validateSquare(square);
        double longitude = -180.0;
        double latitude = -90.0;
        double offset = 20.0 * ((int)square.charAt(0) - valueA);
        longitude += offset;
        offset = 10.0 * ((int)square.charAt(1) - valueA);
        latitude += offset;
        offset = 2.0 * ((int)square.charAt(2) - value0);
        longitude += offset;
        offset = 1.0 * ((int)square.charAt(3) - value0);
        latitude += offset;
        if(square.length() == 6){
            // Final offsets need to center in the square
            offset = 1.0/12 * ((int)square.charAt(4) - valueA) + 1.0/24;
            longitude += offset;
            offset = 1.0/24 * ((int)square.charAt(5) - valueA) + 1.0/48;
            latitude += offset;
        } else {
            longitude += 1.0; // Center within the square for short form
            latitude  += 0.5;
        }
        return new Coordinates(latitude, longitude);
    }

    /**
     * Use the Haversine formula to calculate the great-circle distance in kilometers
     * (from: http://www.movable-type.co.uk/scripts/latlong.html)
     * @param pointA is the reference point
     * @param pointB is the other point
     * @return the distance between the points in kilometers
     */
    public static double distance(Coordinates pointA, Coordinates pointB){
        double R = 6371; //radius of earth in km
        double theta1 = toRadians(pointA.getLatitude());
        double theta2 = toRadians(pointB.getLatitude());
        double deltaTheta = theta2 - theta1;
        double deltaLambda = toRadians(pointB.getLongitude() - pointA.getLongitude());
        double a = Math.sin(deltaTheta/2.0) * Math.sin(deltaTheta/2) +
                   Math.cos(theta1) * Math.cos(theta2) *
                   Math.sin(deltaLambda/2.0) * Math.sin(deltaLambda/2.0);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
    /**
     * Return the bearing from pointA to pointB
     * (from: http://www.movable-type.co.uk/scripts/latlong.html)
     * @param pointA is the reference point
     * @param pointB is the other point
     * @return the angle from pointA to pointB in degress [0.0 - 360.0] 
     */
    public static double bearing(Coordinates pointA, Coordinates pointB){
        double deltaLambda = toRadians(pointB.getLongitude() - pointA.getLongitude());
        double theta1 = toRadians(pointA.getLatitude());
        double theta2 = toRadians(pointB.getLatitude());
        double y = Math.sin(deltaLambda) * Math.cos(theta2);
        double x = Math.cos(theta1) * Math.sin(theta2) -
                   Math.sin(theta1) * Math.cos(theta2) * Math.cos(deltaLambda);
        double deg = toDegrees(Math.atan2(y,x));
        return (deg < 0.0) ? 360.0 + deg : deg;
    }

    /**
     * Compute the distance and bearing from someone in gridA to someone in gridB
     * @param gridA is the Maidenhead grid square (4 or 6 characters) for the reference point
     * @param gridB is the same for the destination point
     * @return the distance (km) and bearing (deg) from A to B
     */
    public static DistanceAndBearing distanceAndBearing(String gridA, String gridB){
        Coordinates pointA = midpoint(gridA);
        Coordinates pointB = midpoint(gridB);
        double distance = distance(pointA, pointB);
        double bearing = bearing(pointA, pointB);
        return new DistanceAndBearing(distance, bearing);
    }
    /**
     *  Alias for the method using coordinates
     * @param latitude of the point in question
     * @param longitude of the point in question
     * @param precision is either 4 or 6 indicating the number of characters in the Maidenhead string
     * @return the Maidenhead square string for the coordinates
     */
    public static String toGrid(double latitude, double longitude, int precision){
        return toGrid(new Coordinates(latitude, longitude), precision);
    }
    /**
     * Main method to calculate grid string from lat/lon coordinates
     * @param coordinates is the lat/long of the point in question
     * @param precision is either 4 or 6 indicating the number of characters in the Maidenhead string
     * @return the Maidenhead square string for the coordinates
     */
    public static String toGrid(Coordinates coordinates, int precision){
        if(precision != 4 && precision !=6){
            throw new IllegalArgumentException("Precision can only be 4 or 6 characters");
        }
        double latitude = coordinates.getLatitude();
        double longitude = coordinates.getLongitude();
        StringBuilder sb = new StringBuilder();
        int long_offset = (int)((longitude + 180.0)/20.0);
        char c = (char)(valueUpperA + long_offset);
        sb.append(c);
        int lat_offset = (int)((latitude + 90.0)/10.0);
        c = (char)(valueUpperA + lat_offset);
        sb.append(c);
        int long_square = (int)((longitude + 180.0 - 20 * long_offset)/2.0);
        c = (char)(value0 + long_square);
        sb.append(c);
        int lat_square = (int)(latitude + 90.0 - 10 * lat_offset);
        c = (char)(value0 + lat_square);
        sb.append(c);
        if(precision == 6){
            int long_subsquare =  (int)(12.0*(longitude + 180.0 - 20.0 * long_offset - 2.0 * long_square)); // 5' is 1/12 of a degree
            c = (char)(valueA + long_subsquare);
            sb.append(c);
            int lat_subsquare = (int)(24.0*(latitude + 90.0 - 10 * lat_offset - lat_square)); // 2.5' is 1/24 of a degree
            c = (char)(valueA + lat_subsquare);
            sb.append(c);
        }
        return sb.toString();
    }
}