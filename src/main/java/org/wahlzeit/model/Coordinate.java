package org.wahlzeit.model;

public interface Coordinate {

    final double epsilon = 0.00001d;

    public CartesianCoordinate asCartesianCoordinate() ;
    public double getCartesianDistance(Coordinate c) throws IllegalArgumentException;
    public SphericCoordinate asSphericCoordinate();
    public double getCentralAngle(Coordinate c) throws IllegalArgumentException;
    public boolean isEqual(Coordinate c);
    //Every Coordinate should provide this assertion method, and it is public to enable
    // checking the class invariants from outside (i.e. before writing them to the database)
    public void assertClassInvariants();
}
