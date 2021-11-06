package org.wahlzeit.model;

import java.sql.*;
import java.net.*;
import java.lang.Math.*;

import org.wahlzeit.services.*;
import org.wahlzeit.utils.*;

public class Coordinate{

    private double x;
    private double y;
    private double z;

    public Coordinate(double x, double y, double z){
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

    public double getDistance(Coordinate c){
        //Just calculating the formula for cartesian distance
        return Math.sqrt(Math.pow(c.x - this.x, 2) + Math.pow(c.y - this.y, 2) + Math.pow(c.z - this.z, 2));
    }

    public boolean isEqual(Coordinate c){
        if(c.x != this.x || c.y != this.y || c.z != this.z)return false;
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (o.getClass() != this.getClass()) {
            return false;
        }

        Coordinate compare = (Coordinate) o;
        return isEqual(compare);
    }
}