package org.wahlzeit.model;

public abstract class AbstractCoordinate implements Coordinate{

    @Override
    public double getCartesianDistance(Coordinate c){
        CartesianCoordinate self = this.asCartesianCoordinate();
        CartesianCoordinate other = c.asCartesianCoordinate();
        return Math.sqrt(Math.pow(other.x - self.x, 2) + Math.pow(other.y - self.y, 2) + Math.pow(other.z - self.z, 2));
    }

    @Override
    public double getCentralAngle(Coordinate c) {
        SphericCoordinate self = this.asSphericCoordinate();
        SphericCoordinate other = c.asSphericCoordinate();
        return Math.acos(Math.sin(self.theta)* Math.sin(other.theta) + Math.cos(self.theta) * Math.cos(other.theta) * Math.cos(Math.abs(self.phi - other.phi)));
    }
}
