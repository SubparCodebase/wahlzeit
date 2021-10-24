package org.wahlzeit.model;

import java.sql.*;
import java.net.*;

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
}