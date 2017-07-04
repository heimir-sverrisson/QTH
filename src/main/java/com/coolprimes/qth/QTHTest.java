package com.coolprimes.qth;

class QTHTest {
    public static void main(String[] args){
        System.out.println("W1ANT: " + QTH.toGrid(new Coordinates(40.0150, -105.271), 6)); // expect DN70ia
        System.out.println("W1A: " + QTH.toGrid(new Coordinates(42.352, -71.5079), 6)); // expect FN42fi
    }
}