package org.wahlzeit.model;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class CoordinateTest{

    private static List<Coordinate> testCoords;

    //Creating the test coordinates before test execution
    @BeforeClass
    public static void initCoordinateTest(){
        testCoords = new LinkedList<Coordinate>();
        testCoords.add(new Coordinate(0,0,0));
        testCoords.add(new Coordinate(1,2,3));
        testCoords.add(new Coordinate(5,-2,4));
        testCoords.add(new Coordinate(-12,1,8));
    }

    //Currently not needed
    /*@Test
    public void creationTest(){

    }*/

    //Calculates distances between all coordinates and compares them to desired results.
    @Test
    public void distanceTest(){
        //Distances calculated by hand (should be correct)
        List<Double> solution = Arrays.asList(new Double[]{0.0, 3.741657, 6.708204, 14.456832,
                                                            3.741657, 0.0, 5.744563, 13.96424,
                                                            6.708204, 5.744563, 0.0, 17.720045,
                                                            14.456832, 13.96424, 17.720045, 0.0});

        //Calculate distances between all coordinates
        List<Double> ergs = new LinkedList<Double>();
        for (Coordinate c: testCoords) {
            for (Coordinate c2: testCoords) {
                ergs.add(c.getDistance(c2));
            }
        }
        System.out.println("Results for distanceTest");
        for (int i = 0; i < ergs.size(); i++){
            System.out.println("Actual: " + ergs.get(i).toString() + "  Target: " + solution.get(i).toString());
            assertEquals(ergs.get(i), solution.get(i), 0.0001);
        }
    }

    //Checking if equal and unequal coordinates are correctly detected
    @Test
    public void isEqualTest(){
        for (int i = 0; i < testCoords.size(); i++){
            for (int j = 0; j < testCoords.size(); j++){
                if(i == j){
                    assertTrue(testCoords.get(i).isEqual(testCoords.get(j)));
                }else {
                    assertFalse(testCoords.get(i).isEqual(testCoords.get(j)));
                }
            }
        }
    }

    //Same as isEqualTest but equals() is used.
    @Test
    public void equalsTest(){
        //
        for (int i = 0; i < testCoords.size(); i++){
            for (int j = 0; j < testCoords.size(); j++){
                if(i == j){
                    assertTrue(testCoords.get(i).equals(testCoords.get(j)));
                }else {
                    assertFalse(testCoords.get(i).equals(testCoords.get(j)));
                }
            }
        }
    }
}