package org.wahlzeit.model;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.wahlzeit.agents.AgentManager;
import org.wahlzeit.main.ServiceMain;
import org.wahlzeit.services.EmailAddress;
import org.wahlzeit.services.Language;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PersistenceTest {

    //Start the service Main instance to test interactions with the database.
    @BeforeClass
    public static void initTest() throws Exception {
        try {
            ServiceMain.getInstance().startUp(false, System.getProperty("user.dir") + "/build/inplaceWebapp");
        }catch (IllegalStateException e){
            //Occurs when ServiceMain was already started previously.
            //Do nothing
        }catch (IllegalThreadStateException e){
            //Occurs when the agent manager tries to start all thread when they were previously started.
            //Also do nothing
        }catch (RuntimeException e){
            System.out.println("This Exception occurs if the database is not running.");
            System.out.println("Please make sure the database is running and restart the test.");
            throw e;
        }
    }

    //Inserts a location in the database, then clears the manager cache to check if the location can be retrieved from the database.
    @Test
    public void insertLocationTest(){
        LocationManager lm = LocationManager.getInstance();
        Location l = lm.createLocation("Earths core", 0,0,0);
        lm.saveLocation(l); //Location should be in the database now
        LocationId testLocationId = l.getId();
        lm.locationCache.clear(); //This prevents getLocationFromId() to just take the location from the cache without accessing the database.
        Location location = lm.getLocationFromId(testLocationId); //Location should now be read from the database.
        assertTrue(l.isEqual(location));
    }

    //It is not possible to Test Persistence over a restart as ServiceMain would need to be supplied with a new AgentManager as the stopped Threads cant be started.
    //As such this Test can be used to check if a location can be read from the database after it was added in a previous instance.
    //Commented out as this Test cannot be run without previous setup.
    //@Test
    public void retrieveLocationSepInstanceTest(){
        //Change value of this to change to Location to compare to. This Location needs to be added before this Test can be run.
        Location testLocation = new Location(LocationId.getIdFromInt(8), "The Void", -1, -1, -1);

        LocationManager lm = LocationManager.getInstance();
        Location location = lm.getLocationFromId(testLocation.getId());
        assertTrue(location.isEqual(testLocation));
    }

    //Inserts a photo in the database, then clears the manager cache to check if the photo can be retrieved from the database.
    @Test
    public void insertImageTest() throws Exception {
        PhotoManager pm = PhotoManager.getInstance();
        String pathname = System.getProperty("user.dir");
        File f = new File(pathname + "/src/testData/TESTPIC1.jpg"); //Get test picture
        Photo p = pm.createPhoto(f); //Create Photo from picture

        //Add data
        p.setOwnerName("TEST NAME");
        p.setOwnerLanguage(Language.GERMAN);
        p.setOwnerHomePage(new URL("https:\\www.no.com"));
        p.setTags(new Tags("testTag"));
        p.setOwnerId(1);
        p.setOwnerEmailAddress(EmailAddress.getFromString("testname@default.net"));

        pm.savePhoto(p); //Photo should be in the database now
        PhotoId testPhotoId = p.getId();
        pm.photoCache.clear(); //This prevents getPhotoFromId() to just take the photo from the cache without accessing the database.
        Photo photo = pm.getPhotoFromId(testPhotoId); //Photo should now be read from the database.

        //Check if read photo data matches (just one check should suffice)
        assertEquals(photo.getOwnerName(), "TEST NAME");
    }

    //Inserts a photo with associated location in the database, then clears the manager caches to check if the photo and location can be retrieved from the database.
    @Test
    public void insertImageWithLocationTest() throws Exception {
        PhotoManager pm = PhotoManager.getInstance();
        String pathname = System.getProperty("user.dir");
        File f = new File(pathname + "/src/testData/TESTPIC2.jpg"); //Get test picture
        Photo p = pm.createPhoto(f); //Create Photo from picture

        //Add data
        p.setOwnerName("LOCATION OWNER");
        p.setOwnerLanguage(Language.GERMAN);
        p.setOwnerHomePage(new URL("https:\\\\www.earths-core.com"));
        p.setTags(new Tags("earths_core"));
        p.setOwnerId(1);
        p.setOwnerEmailAddress(EmailAddress.getFromString("owner@earths-core.com"));

        //Create and add location
        LocationManager lm = LocationManager.getInstance();
        Location l = lm.createLocation("Earths core", 0,0,0);
        p.setLocation(l);

        pm.savePhoto(p); //Photo and location should be in the database now
        PhotoId testPhotoId = p.getId();

        //Clear caches for reasons as before
        pm.photoCache.clear();
        lm.locationCache.clear();

        Photo photo = pm.getPhotoFromId(testPhotoId); //Photo with location should now be read from the database.

        //Check read photo and location
        assertEquals(photo.getOwnerName(), "LOCATION OWNER");
        assertTrue(photo.getLocation().isEqual(l));
        assertEquals(photo.getLocation().getId(), l.getId());
    }

    //Inserts a photo with associated already in the database existing location in the database,
    // then clears the manager caches to check if the photo can be retrieved from the database.
    @Test
    public void insertImageWithExistingLocationTest() throws Exception {
        //Everything as in insertImageWithLocationTest
        PhotoManager pm = PhotoManager.getInstance();
        String pathname = System.getProperty("user.dir");
        File f = new File(pathname + "/src/testData/TESTPIC3.jpg");
        Photo p = pm.createPhoto(f);
        p.setOwnerName("LOCATION OWNER-IN-CHIEF");
        p.setOwnerLanguage(Language.GERMAN);
        p.setOwnerHomePage(new URL("https:\\\\www.earths-core.com"));
        p.setTags(new Tags("earths_core"));
        p.setOwnerId(1);
        p.setOwnerEmailAddress(EmailAddress.getFromString("owner-in-chief@earths-core.com"));

        //Add last added location to the photo (will be the location from insertImageWithLocationTest)
        LocationManager lm = LocationManager.getInstance();
        Location l = lm.getLocationFromId(LocationId.getIdFromInt(LocationId.getCurrentIdAsInt()));
        p.setLocation(l);

        pm.savePhoto(p); //Photo should be in the database now, no new location should be added to the database
        PhotoId testPhotoId = p.getId();

        //Clear caches for reasons as before
        pm.photoCache.clear();
        lm.locationCache.clear();

        Photo photo = pm.getPhotoFromId(testPhotoId); //Photo with location should now be read from the database.

        //Check read photo and location
        assertEquals(photo.getOwnerName(), "LOCATION OWNER-IN-CHIEF");
        assertTrue(photo.getLocation().isEqual(l));
        assertEquals(photo.getLocation().getId(), l.getId());
    }

}
