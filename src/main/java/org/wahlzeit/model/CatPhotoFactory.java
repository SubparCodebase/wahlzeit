package org.wahlzeit.model;

import org.wahlzeit.services.SysLog;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CatPhotoFactory extends PhotoFactory{

    private static CatPhotoFactory instance = null;

    public static synchronized CatPhotoFactory getInstance(){
        //Preconditions: None
        if (instance == null) {
            SysLog.logSysInfo("setting generic PhotoFactory");
            setInstance(new CatPhotoFactory());
        }
        return instance;
        //Postconditions: None
    }

    protected static synchronized void setInstance(PhotoFactory photoFactory) {
        if (instance != null) {
            throw new IllegalStateException("attempt to initialize PhotoFactory twice");
        }
        instance = (CatPhotoFactory) photoFactory;
    }

    //One create CatPhoto per CatPhoto Constructor, all checks are done in the CatPhoto class itself
    @Override
    public CatPhoto createPhoto() {
        //Preconditions: None
        return new CatPhoto();
        //Postconditions: None
    }

    @Override
    public CatPhoto createPhoto(PhotoId id) {
        //Preconditions: None
        return new CatPhoto(id);
        //Postconditions: None
    }

    public CatPhoto createPhoto(int count) {
        //Preconditions: None
        return new CatPhoto(count);
        //Postconditions: None
    }

    public CatPhoto createPhoto(PhotoId id, int count) {
        //Preconditions: None
        return new CatPhoto(id, count);
        //Postconditions: None
    }

    @Override
    public CatPhoto createPhoto(ResultSet rs) throws SQLException {
        //Preconditions: None
        return new CatPhoto(rs);
        //Postconditions: None
    }
}
