package org.wahlzeit.model;

import java.sql.*;
import java.net.*;
import java.lang.Math.*;
import java.util.Objects;

import org.wahlzeit.services.*;
import org.wahlzeit.utils.*;

public class CartesianCoordinate extends AbstractCoordinate{

    protected double x;
    protected double y;
    protected double z;

    public CartesianCoordinate(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }

    public double getZ(){
        return z;
    }

    public void setX(double x){
        this.x = x;
    }

    public void setY(double y){
        this.y = y;
    }

    public void setZ(double z){
        this.z = z;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (o.getClass() != this.getClass()) {
            return false;
        }

        CartesianCoordinate compare = (CartesianCoordinate) o;
        return isEqual(compare);
    }

    @Override
    public CartesianCoordinate asCartesianCoordinate() {
        return this;
    }

    @Override
    public SphericCoordinate asSphericCoordinate() {
        //r = sqrt(x² + y² + z²)
        double r = Math.sqrt(x*x + y*y + z*z);
        //cos(theta) = z/r;
        double theta = Math.acos(z/r);
        //tan(phi) = y/x;
        double phi = Math.atan(y/x);
        return new SphericCoordinate(phi, theta, r);
    }

    @Override
    public int hashCode(){
        return Objects.hash(x, y, z);
    }

    @Override
    public boolean isEqual(Coordinate c) {
        CartesianCoordinate other = c.asCartesianCoordinate();
        if(Math.abs(other.x - this.x)>epsilon || Math.abs(other.y - this.y)>epsilon || Math.abs(other.z - this.z)>epsilon)return false;
        return true;
    }
}