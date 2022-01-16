package org.wahlzeit.model;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class CatTypesTest {

    public static CatManager cm = CatManager.getInstance();
    //Initialize a basic hierarchy of cat types, based on https://de.wikipedia.org/wiki/Katzen#Gattungen_und_Arten
    @BeforeClass
    public static void initTest(){
        //Ill use German Names here to make it easier for me
        CatType Katze = new CatType("Katze");
        CatType Großkatze = new CatType("Großkatze", Katze);
        CatType Panthera = new CatType("Panthera", Großkatze);
        CatType Jaguar = new CatType("Jaguar", Panthera);
        CatType Tiger = new CatType("Tiger", Panthera);
        CatType Kleinkatze = new CatType("Kleinkatze", Katze);
        CatType Caracal = new CatType("Caracal", Kleinkatze);
        CatType Luchs = new CatType("Luchs", Kleinkatze);
        CatType Echte_Katze = new CatType("Echte_Katze", Kleinkatze);
        assert cm.catTypes.size() == 9;
    }

    //Checks if Subtypes and Supertypes are recognized correctly
    @Test
    public void subTypesTest(){
        boolean subTypeArray[][] = {
                {false, false, false, false, false, false, false, false, false},
                {true, false, false, false, false, false, false, false, false},
                {true, true, false, false, false, false, false, false, false},
                {true, true, true, false, false, false, false, false, false},
                {true, true, true, false, false, false, false, false, false},
                {true, false, false, false, false, false, false, false, false}, //Kleinkatze
                {true, false, false, false, false, true, false, false, false},
                {true, false, false, false, false, true, false, false, false},
                {true, false, false, false, false, true, false, false, false}
        };
        boolean superTypeArray[][] = {
                {false, true, true, true, true, true, true, true, true},
                {false, false, true, true, true, false, false, false, false},
                {false, false, false, true, true, false, false, false, false},
                {false, false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, true, true, true}, //Kleinkatze
                {false, false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false, false}
        };


        CatType types[] = Arrays.copyOf(cm.catTypes.values().toArray(), cm.catTypes.values().toArray().length, CatType[].class);
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                assertTrue(types[i].isSubType(types[j]) == subTypeArray[i][j]);
                assertTrue(types[i].isSuperType(types[j]) == superTypeArray[i][j]);
            }
        }
    }

    //Tests if Cats are created properly
    @Test
    public void catCreationTest(){
        for (String typeName : cm.catTypes.keySet()){
            cm.createCat(typeName);
        }
        assertTrue(cm.catCache.size() == 9);
        for (Cat cat : cm.catCache.values()){
            assertTrue(cm.hasType(cat.getType().name));
        }
    }

    //Tests if hasInstance works
    @Test
    public void hasInstanceTest(){
        boolean katzeInst[] = {true, false, false, false, false, false, false, false, false};
        boolean luchsInst[] = {true, false, false, false, false, true, false, true, false};
        boolean panthInst[] = {true, true, true, false, false, false, false, false, false};

        Cat katze = cm.createCat("Katze");
        Cat luchs = cm.createCat("Luchs");
        Cat panthera = cm.createCat("Panthera");
        int i = 0;
        for(CatType type : cm.catTypes.values()){
            assertTrue(katzeInst[i] == type.hasInstance(katze));
            assertTrue(luchsInst[i] == type.hasInstance(luchs));
            assertTrue(panthInst[i] == type.hasInstance(panthera));
            i++;
        }
    }

}
