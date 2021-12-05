package org.wahlzeit.model;

import java.sql.*;
import java.util.Objects;

import org.wahlzeit.services.*;

public class Location extends DataObject{

    //Using an id to differentiate between locations, as its easier than having the the combination of name and coordinate as primary key.
    private LocationId id = null;
    private String name;
    private Coordinate coordinate;

    public Location(LocationId id, String name, double x, double y, double z) {
        this.id = id;
        this.name = name;
        incWriteCount();
        coordinate = new CartesianCoordinate(x, y, z);
    }

    public Location(LocationId id, String name, Coordinate c) {
        this.id = id;
        this.name = name;
        incWriteCount();
        coordinate = c;
    }

    public Location(String name, double x, double y, double z) {
        this.id = LocationId.getNextId();
        this.name = name;
        incWriteCount();
        coordinate = new CartesianCoordinate(x, y, z);
    }

    public Location(String name, Coordinate c) {
        this.id = LocationId.getNextId();
        this.name = name;
        incWriteCount();
        coordinate = c;
    }

    public Location(ResultSet rset) throws SQLException{
        readFrom(rset);
    }

    public LocationId getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public Coordinate getCoordinate(){
        return coordinate;
    }

    public boolean isEqual(Location l){
        if(!name.equals(l.name) || !coordinate.isEqual(l.coordinate))return false;
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

        Location compare = (Location) o;
        return isEqual(compare);
    }

    @Override
    public int hashCode(){
        return Objects.hash(name, coordinate.hashCode(), id);
    }

    @Override
    public String getIdAsString() {
        return String.valueOf(id.asInt());
    }

    @Override
    public void readFrom(ResultSet rset) throws SQLException {
        id = LocationId.getIdFromInt(rset.getInt("id"));
        name = rset.getString("name");
        //Load as cartesian coordinate, since its saved as such
        coordinate = new CartesianCoordinate(rset.getDouble("x_coordinate"),
                rset.getDouble("y_coordinate"),
                rset.getDouble("z_coordinate"));
    }

    @Override
    public void writeOn(ResultSet rset) throws SQLException {
        rset.updateInt("id", id.asInt());
        rset.updateString("name", name);
        //Save as cartesian coordinate
        CartesianCoordinate c =coordinate.asCartesianCoordinate();
        rset.updateDouble("x_coordinate", c.getX());
        rset.updateDouble("y_coordinate", c.getY());
        rset.updateDouble("z_coordinate", c.getZ());
    }

    @Override
    public void writeId(PreparedStatement stmt, int pos) throws SQLException {
        stmt.setInt(pos, id.asInt());
    }
}