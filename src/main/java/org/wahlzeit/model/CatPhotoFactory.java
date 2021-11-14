package org.wahlzeit.model;

import org.wahlzeit.services.SysLog;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CatPhotoFactory extends PhotoFactory{

    private static CatPhotoFactory instance = null;

    public static synchronized CatPhotoFactory getInstance(){
        if (instance == null) {
            SysLog.logSysInfo("setting generic PhotoFactory");
            setInstance(new CatPhotoFactory());
        }
        return instance;
    }

    protected static synchronized void setInstance(PhotoFactory photoFactory) {
        if (instance != null) {
            throw new IllegalStateException("attempt to initialize PhotoFactory twice");
        }
        instance = (CatPhotoFactory) photoFactory;
    }

    @Override
    public CatPhoto createPhoto() {
        return new CatPhoto();
    }

    @Override
    public CatPhoto createPhoto(PhotoId id) {
        return new CatPhoto(id);
    }

    public CatPhoto createPhoto(int count) {
        return new CatPhoto(count);
    }

    public CatPhoto createPhoto(PhotoId id, int count) {
        return new CatPhoto(id, count);
    }

    @Override
    public CatPhoto createPhoto(ResultSet rs) throws SQLException {
        return new CatPhoto(rs);
    }
}
