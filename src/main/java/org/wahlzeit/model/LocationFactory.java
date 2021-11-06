/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.model;

import java.sql.*;

import org.wahlzeit.services.*;

//Just a copy of PhotoFactory for Location to reduce changes in LocationManager
public class LocationFactory {

    private static LocationFactory instance = null;

    public static synchronized LocationFactory getInstance() {
        if (instance == null) {
            SysLog.logSysInfo("setting generic LocationFactory");
            setInstance(new LocationFactory());
        }

        return instance;
    }

    protected static synchronized void setInstance(LocationFactory locationFactory) {
        if (instance != null) {
            throw new IllegalStateException("attempt to initialize LocationFactory twice");
        }

        instance = locationFactory;
    }

    //Not called since Locations are not directly used in wahlzeit
    public static void initialize() {
        getInstance(); // drops result due to getInstance() side-effects
    }

    protected LocationFactory() {
        // do nothing
    }

    //One create Location per Location Constructor
    public Location createLocation(LocationId id, String name, double x, double y, double z) {
        return new Location(id, name, x, y, z);
    }

    public Location createLocation(LocationId id, String name, Coordinate c) {
        return new Location(id, name, c);
    }

    public Location createLocation(String name, double x, double y, double z) {
        return new Location(name, x, y, z);
    }

    public Location createLocation(String name, Coordinate c) {
        return new Location(name, c);
    }

    public Location createLocation(ResultSet rs) throws SQLException {
        return new Location(rs);
    }
}
