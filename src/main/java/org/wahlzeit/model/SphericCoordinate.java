package org.wahlzeit.model;

import com.sun.mail.util.logging.MailHandler;

public class SphericCoordinate implements Coordinate{

    private double phi; //Azimuth, longitude
    private double theta; //Inclination, latitude
    private double radius;

    //Expecting angle input in radians
    public SphericCoordinate(double phi, double theta, double r){
        //Coordinates are restricted to ensure unique coordinates (also makes equality checks easier)
        //Restrictions chosen according to https://www.sciencedirect.com/topics/computer-science/spherical-polar-coordinate
        if(r < 0){
            throw new IllegalArgumentException("Radius cannot be negative");
        }
        this.phi = phi;
        this.theta = theta;
        this.radius = r;
    }

    public double getPhi(){ return phi; }

    public double getTheta(){
        return theta;
    }

    public double getRadius(){
        return radius;
    }

    public void setPhi(double phi){
        this.phi = phi;
    }

    public void setTheta(double theta){
        this.theta = theta;
    }

    public void setRadius(double r){
        if(r < 0){
            throw new IllegalArgumentException("Radius cannot be negative");
        }
        this.radius = r;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (o.getClass() != this.getClass()) {
            return false;
        }

        SphericCoordinate compare = (SphericCoordinate) o;
        return isEqual(compare);
    }

    @Override
    public CartesianCoordinate asCartesianCoordinate() {
        //3x = r*sin(theta)*cos(phi)
        double x = Math.sin(theta) * Math.cos(phi) * radius;
        //y = r*sin(theta)*sin(phi)
        double y = Math.sin(theta) * Math.sin(phi) * radius;
        //z = r * cos(theta)
        double z = Math.cos(theta) * radius;
        return new CartesianCoordinate(x,y,z);
    }

    @Override
    public SphericCoordinate asSphericCoordinate() {
        return this;
    }

    @Override
    public double getCartesianDistance(Coordinate c) {
        return this.asCartesianCoordinate().getCartesianDistance(c);
    }

    @Override
    public double getCentralAngle(Coordinate c) {
        SphericCoordinate other = c.asSphericCoordinate();
        return Math.acos(Math.sin(theta)* Math.sin(other.theta) + Math.cos(theta) * Math.cos(other.theta) * Math.cos(Math.abs(phi - other.phi)));
    }

    @Override
    public boolean isEqual(Coordinate c) {
        SphericCoordinate other = c.asSphericCoordinate();
        if(Math.abs(other.phi - this.phi)>epsilon || Math.abs(other.theta - this.theta)>epsilon || Math.abs(other.radius - this.radius)>epsilon)return false;
        return true;
    }
}
