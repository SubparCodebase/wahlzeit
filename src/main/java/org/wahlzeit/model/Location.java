package org.wahlzeit.model;

import java.sql.*;
import java.net.*;

import org.wahlzeit.services.*;
import org.wahlzeit.utils.*;

public class Location {

    private Coordinate coordinate;

    public Location(double x, double y, double z) {
        coordinate = new Coordinate(x, y, z);
    }

    public Location(Coordinate c) {
        coordinate = c;
    }
}