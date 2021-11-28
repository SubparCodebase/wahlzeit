package org.wahlzeit.model;

import com.sun.mail.util.logging.MailHandler;

import java.util.Objects;

public class SphericCoordinate extends AbstractCoordinate{

    protected double phi; //Azimuth, longitude
    protected double theta; //Inclination, latitude
    protected double radius;

    //Expecting angle input in radians
    public SphericCoordinate(double phi, double theta, double r){
        if(r < 0){
            throw new IllegalArgumentException("Radius cannot be negative");
        }
        //Keeping Angles within [-2PI, 2PI]
        this.phi = phi%(Math.PI*2);
        this.theta = theta%(Math.PI*2);
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
        this.phi = phi%(Math.PI*2);
    }

    public void setTheta(double theta){
        this.theta = theta%(Math.PI*2);
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
    public int hashCode(){
        return Objects.hash(phi, theta, radius);
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
    public boolean isEqual(Coordinate c) {
        SphericCoordinate other = c.asSphericCoordinate();
        if(Math.abs(other.phi - this.phi)>epsilon || Math.abs(other.theta - this.theta)>epsilon || Math.abs(other.radius - this.radius)>epsilon)return false;
        return true;
    }
}
