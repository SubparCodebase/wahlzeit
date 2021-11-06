package org.wahlzeit.model;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.wahlzeit.agents.AgentManager;
import org.wahlzeit.main.ServiceMain;
import org.wahlzeit.services.EmailAddress;
import org.wahlzeit.services.Language;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class LocationTest {

    private static List<Location> testLocs;

    //Creating the test locations before test execution
    @BeforeClass
    public static void initTest() throws Exception {
        testLocs = new LinkedList<Location>();
        testLocs.add(new Location("Earths core",0,0,0));
        testLocs.add(new Location("The Void", -1,-1,-1));
        testLocs.add(new Location("Java", -2171.807,5942.831,-801.445));
        testLocs.add(new Location("Chad", 5832.214,1985.506,1644.824));
    }

    //Checking if equal and unequal locations are correctly detected
    @Test
    public void isEqualTest(){
        for (int i = 0; i < testLocs.size(); i++){
            for (int j = 0; j < testLocs.size(); j++){
                if(i == j){
                    assertTrue(testLocs.get(i).isEqual(testLocs.get(j)));
                }else {
                    assertFalse(testLocs.get(i).isEqual(testLocs.get(j)));
                }
            }
        }
    }

    //Same as isEqualTest but equals() is used.
    @Test
    public void equalsTest(){
        for (int i = 0; i < testLocs.size(); i++){
            for (int j = 0; j < testLocs.size(); j++){
                if(i == j){
                    assertTrue(testLocs.get(i).equals(testLocs.get(j)));
                }else {
                    assertFalse(testLocs.get(i).equals(testLocs.get(j)));
                }
            }
        }
    }
}
