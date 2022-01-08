/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.model;

import java.io.*;
import java.sql.*;
import java.util.*;

import org.wahlzeit.main.*;
import org.wahlzeit.services.*;

/**
 * Adapted Copy of PhotoManager
 */
@PatternInstance(
        patternName = "Singleton",
        participants = {
                "Singleton"
        }
)
//My reasoning behind this, is that methods like addLocation call doAddLocation, which could be overridden in a
//subclass to change the behavior, while the part that reads from the database stays intact.
//This seems very similar to the "Template Method" design pattern, with the only problem being that there is currently
//no subclass which makes use of this pattern.
@PatternInstance(
        patternName = "Template Method",
        participants = {
                "AbstractClass", "ConcreteClass"
        }
)
public class LocationManager extends ObjectManager {

    protected static final LocationManager instance = new LocationManager();

    protected Map<LocationId, Location> locationCache = new HashMap<LocationId, Location>();

    public static final LocationManager getInstance() {
        //Preconditions: None
        //Postconditions: None
        return instance;
    }

    public static final boolean hasLocation(String id) {
        //Preconditions: None, null check for id is done in getIdFromString
        //Postconditions: None
        return hasLocation(LocationId.getIdFromString(id));
    }

    public static final boolean hasLocation(LocationId id) {
        //Preconditions: None, null check for id is done in getLocation
        //Postconditions: None
        return getLocation(id) != null;
    }

    public static final Location getLocation(String id) {
        //Preconditions: None, null check for id is done in getIdFromString
        //Postconditions: None
        return getLocation(LocationId.getIdFromString(id));
    }

    public static final Location getLocation(LocationId id) {
        //Preconditions: None, null check for id is done in getLocationFromId
        //Postconditions: None
        return instance.getLocationFromId(id);
    }

    public LocationManager() {
        //Do nothing
    }

    protected boolean doHasLocation(LocationId id) {
        //Preconditions: None, null check for id is done in calling method
        //Postconditions: None
        return locationCache.containsKey(id);
    }

    public Location getLocationFromId(LocationId id) {
        //Preconditions: The argument id is not null
        ContractEnforcerUtil.assertArgumentNonNull(id);
        if (id.isNullId()) {
            return null;
        }

        Location result = doGetLocationFromId(id);

        if (result == null) {
            try {
                PreparedStatement stmt = getReadingStatement("SELECT * FROM locations WHERE id = ?");
                result = (Location) readObject(stmt, id.asInt());
            } catch (SQLException sex) {
                SysLog.logThrowable(sex);
            }
            if (result != null) {
                doAddLocation(result);
            }
        }

        //Postconditions: Location is now added to the Cache, if the result was not null
        if(result!=null)assert locationCache.get(id) != null;
        return result;
    }

    protected Location doGetLocationFromId(LocationId id) {
        //Preconditions: None, null check for id is done in calling method
        //Postconditions: None
        return locationCache.get(id);
    }

    protected Location createObject(ResultSet rset) throws SQLException {
        //Preconditions: None, null check for rset is done in createLocation
        //Postconditions: None
        return LocationFactory.getInstance().createLocation(rset);
    }

    public void addLocation(Location location) {
        //Preconditions: The argument location is not null
        ContractEnforcerUtil.assertArgumentNonNull(location);
        LocationId id = location.getId();
        assertIsNewLocation(id);
        doAddLocation(location);

        try {
            PreparedStatement stmt = getReadingStatement("INSERT INTO locations(id) VALUES(?)");
            createObject(location, stmt, id.asInt());
            ServiceMain.getInstance().saveGlobals();
        } catch (SQLException sex) {
            SysLog.logThrowable(sex);
        }
        //Postconditions: Location is now added to the Cache
        assert locationCache.get(id) != null;
    }

    protected void doAddLocation(Location myLocation) {
        //Preconditions: None, null check for myLocation is done in calling method
        locationCache.put(myLocation.getId(), myLocation);
        //Postconditions: None
    }

    public void loadLocations(Collection<Location> result) {
        //Preconditions: The argument result is not null
        ContractEnforcerUtil.assertArgumentNonNull(result);
        try {
            PreparedStatement stmt = getReadingStatement("SELECT * FROM locations");
            readObjects(result, stmt);
            for (Iterator<Location> i = result.iterator(); i.hasNext(); ) {
                Location location = i.next();
                if (!doHasLocation(location.getId())) {
                    doAddLocation(location);
                } else {
                    SysLog.logSysInfo("location", location.getId().asString(), "location had already been loaded");
                }
            }
        } catch (SQLException sex) {
            SysLog.logThrowable(sex);
        }

        //Postconditions: All locations are now in the Cache
        for (Location location : result) {
            assert locationCache.get(location.getId()) != null;
        }
        SysLog.logSysInfo("loaded all locations");

    }

    public void saveLocation(Location location) {
        //Preconditions: The argument location is not null
        ContractEnforcerUtil.assertArgumentNonNull(location);
        try {
            PreparedStatement stmt = getUpdatingStatement("SELECT * FROM locations WHERE id = ?");
            updateObject(location, stmt);
        } catch (SQLException sex) {
            SysLog.logThrowable(sex);
        }
        //Postconditions: None
    }

    public void saveLocations() {
        //Preconditions: None
        try {
            PreparedStatement stmt = getUpdatingStatement("SELECT * FROM locations WHERE id = ?");
            updateObjects(locationCache.values(), stmt);
        } catch (SQLException sex) {
            SysLog.logThrowable(sex);
        }
        //Postconditions: None
    }

    protected PreparedStatement getUpdatingStatementFromConditions(int no) throws SQLException {
        //Preconditions: None
        String query = "SELECT * FROM tags";
        if (no > 0) {
            query += " WHERE";
        }

        for (int i = 0; i < no; i++) {
            if (i > 0) {
                query += " OR";
            }
            query += " (tag = ?)";
        }

        //Postconditions: None
        return getUpdatingStatement(query);
    }

    public Location createLocation(String name, double x, double y, double z) {
        //Preconditions: None, null check for name is done in calling method
        LocationId id = LocationId.getNextId();
        Location result = LocationFactory.getInstance().createLocation(id, name, x, y, z);
        addLocation(result);
        //Postconditions: Location is now added to the Cache, this is checked in addLocation
        return result;
    }

    public Location createLocation(String name, Coordinate c) {
        //Preconditions: None, null check for name and c is done in calling method
        LocationId id = LocationId.getNextId();
        Location result = LocationFactory.getInstance().createLocation(id, name, c);
        addLocation(result);
        //Postconditions: Location is now added to the Cache, this is checked in addLocation
        return result;
    }

    /**
     * @methodtype assertion
     */
    protected void assertIsNewLocation(LocationId id) {
        //Preconditions: None, null check for id is done in hasLocation
        if (hasLocation(id)) {
            throw new IllegalStateException("Location already exists!");
        }
        //Postconditions: None
    }

    public void assertClassInvariants() {
        //There is nothing to check here, method added for completeness, will not be called
    }

}
