package org.wahlzeit.model;

import org.junit.Test;

public class IntermediateTest {

    //@Test
    public void BaseTest(){
        Photo p = CatPhotoManager.getInstance().getPhotoFromFilter(new PhotoFilter());
        Class c = p.getClass();
        System.out.println("A");
    }
}
