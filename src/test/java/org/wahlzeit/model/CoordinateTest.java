package org.wahlzeit.model;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class CoordinateTest{

    private static final double pi = Math.PI;
    private static List<Coordinate> testCoords;

    //Creating the test coordinates before test execution
    @BeforeClass
    public static void initCoordinateTest(){
        //Remove all current coordinates that may have been added by other tests
        CartesianCoordinate.coordList.clear();
        SphericCoordinate.coordList.clear();

        testCoords = new LinkedList<Coordinate>();
        testCoords.add(CartesianCoordinate.getCartesianCoordinate(1,1,1));
        testCoords.add(CartesianCoordinate.getCartesianCoordinate(1,2,3));
        testCoords.add(CartesianCoordinate.getCartesianCoordinate(5,-2,4));
        testCoords.add(CartesianCoordinate.getCartesianCoordinate(-12,1,8));
        testCoords.add(SphericCoordinate.getSphericCoordinate(0, 1, 1));
        testCoords.add(SphericCoordinate.getSphericCoordinate(0.5, 0.25, 5.5));
        testCoords.add(SphericCoordinate.getSphericCoordinate(-0.379, 0.441, 2.75));
        testCoords.add(SphericCoordinate.getSphericCoordinate(0.733, -0.294, 12.3));
        assertTrue(CartesianCoordinate.coordList.size() == 4);
        assertTrue(SphericCoordinate.coordList.size() == 4);
    }

    //
    @Test
    public void conversionTest(){
        //Coordinates converted to their counterpart by hand (should be correct),
        // also checks if conversion does not create a new object in the shared object list, since it is already present

        List<Coordinate> converted = new LinkedList<Coordinate>();
        converted.add(SphericCoordinate.getSphericCoordinate(0.785398,0.955316,1.732050));
        converted.add(SphericCoordinate.getSphericCoordinate(1.107148, 0.640522, 3.741657));
        converted.add(SphericCoordinate.getSphericCoordinate(-0.380506, 0.931931, 6.708203));
        converted.add(SphericCoordinate.getSphericCoordinate(-0.083141, 0.984389, 14.456832));
        converted.add(CartesianCoordinate.getCartesianCoordinate(0.841470,0,0.540302));
        converted.add(CartesianCoordinate.getCartesianCoordinate(1.194145, 0.652364, 5.329018));
        converted.add(CartesianCoordinate.getCartesianCoordinate(1.09052, -0.434303, 2.486894));
        converted.add(CartesianCoordinate.getCartesianCoordinate(-2.648904, -2.3849, 11.772236));

        //New Coordinates may already be present depending on test order
        int cartesianListCurrSize = CartesianCoordinate.coordList.size();
        int sphericListCurrSize = SphericCoordinate.coordList.size();

        //Checks if values AND references are equal
        for(int i = 0; i < testCoords.size(); i++){
            if(testCoords.get(i).getClass() == CartesianCoordinate.class){
                SphericCoordinate compareConvert = testCoords.get(i).asSphericCoordinate();
                assertTrue(compareConvert.isEqual(converted.get(i)) && compareConvert.equals(converted.get(i)));
            }else{
                CartesianCoordinate compareConvert = testCoords.get(i).asCartesianCoordinate();
                assertTrue(compareConvert.isEqual(converted.get(i)) && compareConvert.equals(converted.get(i)));
            }
        }

        //The conversion should not have created new objects, since they are already present
        assertTrue(CartesianCoordinate.coordList.size() == cartesianListCurrSize);
        assertTrue(SphericCoordinate.coordList.size() == sphericListCurrSize);
    }

    //Calculates distances between all coordinates and compares them to desired results.
    @Test
    public void distanceTest(){
        //Distances calculated by hand (should be correct)
        List<Double> solution = Arrays.asList(
                0.0,2.23606797749979,5.8309518948453,14.7648230602334,1.11195927022887,4.34729138847995,2.06791611551662,11.8664711352908,
                2.23606797749979,0.0,5.74456264653803,13.9642400437689,3.17415251605454,2.6978027352116,2.48943908295148,10.4639377055837,
                5.8309518948453,5.74456264653803,0.0,17.7200451466694,5.7673973077096,4.82554199078139,4.47492113751679,10.9115328713128,
                14.7648230602334,13.9642400437689,17.7200451466694,0.0,14.8845714329133,13.4662717260422,14.2763194751995,10.6362732502716,
                1.11195927022887,3.17415251605454,5.7673973077096,14.8845714329133,0.0,4.84579821165674,2.00994202174825,12.0011174233542,
                4.34729138847995,2.6978027352116,4.82554199078139,13.4662717260422,4.84579821165674,0.0,3.0445450892775,8.09376782140191,
                2.06791611551662,2.48943908295148,4.47492113751679,14.2763194751995,2.00994202174825,3.0445450892775,0.0,10.1983188465489,
                11.8664711352908,10.4639377055837,10.9115328713128,10.6362732502716,12.0011174233542,8.09376782140191,10.1983188465489,0.0
        );

        //Calculate distances between all coordinates
        List<Double> ergs = new LinkedList<Double>();
        for (Coordinate c: testCoords) {
            for (Coordinate c2: testCoords) {
                ergs.add(c.getCartesianDistance(c2));
            }
        }
        System.out.println("Results for distanceTest");
        for (int i = 0; i < ergs.size(); i++){
            System.out.println("Actual: " + ergs.get(i).toString() + "  Target: " + solution.get(i).toString());
            assertEquals(ergs.get(i), solution.get(i), 0.0001);
        }
    }

    @Test
    public void centralAngleTest(){
        //Central angles calculated by hand (should be correct)
        List<Double> solution = Arrays.asList(
                0.0,0.384182695409057,0.658233470087028,0.481157608404194,0.43317960000567,0.739540490262083,0.982542141226328,1.25011585923333,
                0.384182695409057,0.0,1.02458687681023,0.846573137877783,0.80013406661621,0.666696562684886,1.24887419542775,0.999034399595336,
                0.658233470087028,1.02458687681023,0.0,0.178322014962499,0.225684071470848,0.968548430099378,0.49093248028267,1.55145076003425,
                0.481157608404194,0.846573137877783,0.178322014962499,0.0,0.048059075841621,0.858423909378172,0.584082344857946,1.44905115787987,
                0.43317960000567,0.80013406661621,0.225684071470848,0.048059075841621,0.0,0.839812207355446,0.621318287119513,1.42986645841599,
                0.739540490262083,0.666696562684886,0.968548430099378,0.858423909378172,0.839812207355446,0.0,0.843894954636295,0.5906357397747,
                0.982542141226328,1.24887419542775,0.49093248028267,0.584082344857946,0.621318287119513,0.843894954636295,0.0,1.30816624568802,
                1.25011585923333,0.999034399595336,1.55145076003425,1.44905115787987,1.42986645841599,0.5906357397747,1.30816624568802,0.0
        );

        //Calculate central angle between all coordinates
        List<Double> ergs = new LinkedList<Double>();
        for (Coordinate c: testCoords) {
            for (Coordinate c2: testCoords) {
                ergs.add(c.getCentralAngle(c2));
            }
        }
        System.out.println("Results for centralAngleTest");
        for (int i = 0; i < ergs.size(); i++){
            System.out.println("Actual: " + ergs.get(i).toString() + "  Target: " + solution.get(i).toString());
            assertEquals(ergs.get(i), solution.get(i), 0.0001);
        }
    }

    //@Test
    public void interchangeabilityTest(){
        //Interchangability is already tested in the other tests, so this is currently not neccessary
    }

    //Checking if equal and unequal coordinates are correctly detected by comparing attributes
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

    //Same as isEqualTest but equals() is used, as such only the references are compared, which should work as we used shared value objects
    @Test
    public void equalsTest(){
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

    @Test
    public void hashCodeTest(){
        for (int i = 0; i < testCoords.size(); i++){
            for (int j = 0; j < testCoords.size(); j++){
                if(i == j){
                    assertTrue(testCoords.get(i).hashCode() == testCoords.get(j).hashCode());
                }else {
                    assertFalse(testCoords.get(i).hashCode() == testCoords.get(j).hashCode());
                }
            }
        }
    }
}