package org.wahlzeit.model;

public abstract class AbstractCoordinate implements Coordinate{

    @Override
    public double getCartesianDistance(Coordinate c){
        //Preconditions: Argument is not null
        ContractEnforcerUtil.assertArgumentNonNull(c);
        //Postconditions: None
        //Class Invariants will be checked in the methods called here, so no assertions are needed here.
        return this.asCartesianCoordinate().doGetCartesianDistance(c.asCartesianCoordinate());
    }

    @Override
    public double getCentralAngle(Coordinate c) {
        //Preconditions: Argument is not null
        ContractEnforcerUtil.assertArgumentNonNull(c);
        //Postconditions: None
        //Class Invariants will be checked in the methods called here, so no assertions are needed here.
        return this.asSphericCoordinate().doGetCentralAngle(c.asSphericCoordinate());
    }
}
