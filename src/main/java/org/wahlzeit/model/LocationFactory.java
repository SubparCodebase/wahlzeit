/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.model;

import java.sql.*;

import org.wahlzeit.services.*;

//Just a copy of PhotoFactory for Location to reduce changes in LocationManager
@PatternInstance(
        patternName = "Abstract Factory",
        participants = {
                "AbstractFactory", "ConcreteFactory"
        }
)
@PatternInstance(
        patternName = "Factory Method",
        participants = {
                "Creator", "ConcreteCreator"
        }
)
@PatternInstance(
        patternName = "Singleton",
        participants = {
                "Singleton"
        }
)
public class LocationFactory {

    private static LocationFactory instance = null;

    public static synchronized LocationFactory getInstance() {
        //Preconditions: None
        if (instance == null) {
            SysLog.logSysInfo("setting generic LocationFactory");
            setInstance(new LocationFactory());
        }

        //Postconditions: None
        return instance;
    }

    protected static synchronized void setInstance(LocationFactory locationFactory) {
        ContractEnforcerUtil.assertArgumentNonNull(locationFactory);
        if (instance != null) {
            throw new IllegalStateException("attempt to initialize LocationFactory twice");
        }

        instance = locationFactory;
    }

    //Not called since Locations are not directly used in wahlzeit
    public static void initialize() {
        //Preconditions: None
        getInstance(); // drops result due to getInstance() side-effects
        //Postconditions: None
    }

    protected LocationFactory() {
        // do nothing
    }

    //One create Location per Location Constructor, all checks are done in the location class itself
    public Location createLocation(LocationId id, String name, double x, double y, double z) {
        //Preconditions: None
        return new Location(id, name, x, y, z);
        //Postconditions: None
    }

    public Location createLocation(LocationId id, String name, Coordinate c) {
        //Preconditions: None
        return new Location(id, name, c);
        //Postconditions: None
    }

    public Location createLocation(String name, double x, double y, double z) {
        //Preconditions: None
        return new Location(name, x, y, z);
        //Postconditions: None
    }

    public Location createLocation(String name, Coordinate c) {
        //Preconditions: None
        return new Location(name, c);
        //Postconditions: None
    }

    public Location createLocation(ResultSet rs) throws SQLException {
        //Preconditions: None
        return new Location(rs);
        //Postconditions: None
    }

    public void assertClassInvariants() {
        //There is nothing to check here, method added for completeness, will not be called
    }
}
