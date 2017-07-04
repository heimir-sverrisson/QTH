package com.coolprimes.qth;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class MaidenHeadTest {
    @Test
    public void testToGrid(){
        assertEquals("W1ANT","DN70ia", QTH.toGrid(new Coordinates(40.0150, -105.271), 6));
    }
    @Test
    public void testArguments(){
        assertEquals("W1ANT","DN70ia", QTH.toGrid(40.0150, -105.271, 6));
        try{
            QTH.toGrid(new Coordinates(90.1, 100.0), 6);
        } catch(IllegalArgumentException e){
            assertEquals("Latitude error", e.getMessage(), "Latitude must be between -90 and 90");
        }
        try{
            QTH.toGrid(new Coordinates(-90.1, 100.0), 6);
        } catch(IllegalArgumentException e){
            assertEquals("Latitude error", e.getMessage(), "Latitude must be between -90 and 90");
        }
        try{
            QTH.toGrid(new Coordinates(89.3, -180.1), 6);
        } catch(IllegalArgumentException e){
            assertEquals("Longitude error", e.getMessage(), "Longitude must be between -180 and 180");
        }
        try{
            QTH.toGrid(new Coordinates(89.3, 180.1), 6);
        } catch(IllegalArgumentException e){
            assertEquals("Longitude error", e.getMessage(), "Longitude must be between -180 and 180");
        }
        try{
            QTH.toGrid(new Coordinates(89.3, 100.1), 5);
        } catch(IllegalArgumentException e){
            assertEquals("Precision error", e.getMessage(), "Precision can only be 4 or 6 characters");
        }
    }

    @Test
    public void testDistance(){
        // Distance between the pole and equator is 10,000 km by definition
        assertEquals(10000.0, QTH.distance(new Coordinates(90.0, 0.0), new Coordinates(0.0, 0.0)), 8.0);
        // And the same in both directions
        assertEquals(10000.0, QTH.distance(new Coordinates(0.0, 0.0), new Coordinates(90.0, 0.0)), 8.0);
    }

    @Test
    public void testBearing(){
        // Bearing from equator to North Pole is 0.0 or North
        assertEquals(0.0, QTH.bearing(new Coordinates(0.0, 0.0), new Coordinates(90.0, 0.0)), 0.0001);
        // The other way must be South or 180 degrees
        assertEquals(180.0, QTH.bearing(new Coordinates(90.0, 0.0), new Coordinates(0.0, 0.0)), 0.0001);
        // On the equator we can only be East or West
        assertEquals(90.0, QTH.bearing(new Coordinates(0.0, -100.0), new Coordinates(0.0, 0.0)), 0.0001);
        assertEquals(270.0, QTH.bearing(new Coordinates(0.0, 0.0), new Coordinates(0.0, -100.0)), 0.0001);
    }

    @Test
    public void testMidpointArguments(){
        try{
            QTH.midpoint("AA");
        } catch(IllegalArgumentException e){
            assertEquals(e.getMessage(), "Square can only be 4 or 6 characters");
        }
        try{
            QTH.midpoint("7R70");
        } catch(IllegalArgumentException e){
            assertEquals(e.getMessage(), "First character must be in [A-R]");
        }
        try{
            QTH.midpoint("AS70");
        } catch(IllegalArgumentException e){
            assertEquals(e.getMessage(), "Second character must be in [A-R]");
        }
        try{
            QTH.midpoint("AR7Aii");
        } catch(IllegalArgumentException e){
            assertEquals(e.getMessage(), "Fourth character must be in [0-9]");
        }
        try{
            QTH.midpoint("AR70iy");
        } catch(IllegalArgumentException e){
            assertEquals(e.getMessage(), "Sixth character must be in [A-X]");
        }
    }

    @Test
    public void testMidpointOfASquare(){
        Coordinates p = QTH.midpoint("DN70ja");
        assertEquals(40.020 , p.getLatitude(), 0.01);
        assertEquals(-105.208, p.getLongitude(), 0.01);
    }

    @Test
    public void testMidpointOfASquare2(){
        Coordinates p = QTH.midpoint("AA00");
        assertEquals(-89.5, p.getLatitude(), 0.01);
        assertEquals(-179.0, p.getLongitude(), 0.01);
    }

    @Test
    public void testMidpointOfASquare3(){
        Coordinates p = QTH.midpoint("RR99");
        assertEquals(89.5, p.getLatitude(), 0.01);
        assertEquals(179.0, p.getLongitude(), 0.01);
    }
}
