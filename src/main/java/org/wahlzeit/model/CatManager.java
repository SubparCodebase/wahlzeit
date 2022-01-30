package org.wahlzeit.model;

import org.wahlzeit.main.ServiceMain;
import org.wahlzeit.services.ObjectManager;
import org.wahlzeit.services.Persistent;
import org.wahlzeit.services.SysLog;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class CatManager extends ObjectManager {

    //Manager-Collaboration with Cat; Bound role: Manager
    protected static final CatManager instance = new CatManager();

    protected Map<CatId, Cat> catCache = new HashMap<CatId, Cat>();

    //LinkedHashMap here to prevent the ordering of the keys. (Would lead to failed tests, and i dont want to redo the CompareArrays)
    protected Map<String, CatType> catTypes = new LinkedHashMap<String, CatType>();

    public static CatManager getInstance() {
        //Preconditions: None
        //Postconditions: None
        return instance;
    }

    public static final boolean hasCat(String id) {
        //Preconditions: None, null check for id is done in getIdFromString
        //Postconditions: None
        return hasCat(CatId.getIdFromString(id));
    }

    public static final boolean hasCat(CatId id) {
        //Preconditions: None, null check for id is done in getCat
        //Postconditions: None
        return getCat(id) != null;
    }

    public static final boolean hasType(String typeName){
        return getType(typeName) != null;
    }

    public static final Cat getCat(String id) {
        //Preconditions: None, null check for id is done in getIdFromString
        //Postconditions: None
        return getCat(CatId.getIdFromString(id));
    }

    public static final Cat getCat(CatId id) {
        //Preconditions: None, null check for id is done in getCatFromId
        //Postconditions: None
        return instance.getCatFromId(id);
    }

    //Documentation for Homework cw11 - object creation of Cat
    //Call to CatManager.getType()
    public static final CatType getType(String typeName){
        return instance.getTypeFromName(typeName);
    }

    public CatManager() {
        //Preconditions: None
        //Postconditions: None
    }

    public Cat getCatFromId(CatId id) {
        //Preconditions: The argument id is not null
        ContractEnforcerUtil.assertArgumentNonNull(id);
        if (id.isNullId()) {
            return null;
        }

        return catCache.get(id);
    }

    public CatType getTypeFromName(String typeName){
        ContractEnforcerUtil.assertArgumentNonNull(typeName);
        return catTypes.get(typeName);
    }

    public void addCat(Cat cat) {
        //Preconditions: The argument cat is not null
        ContractEnforcerUtil.assertArgumentNonNull(cat);
        CatId id = cat.getId();
        assertIsNewCat(id);
        catCache.put(cat.getId(), cat);

        //Postconditions: Cat is now added to the Cache
        assert catCache.get(id) != null;
    }

    public void addType(CatType type) {
        //Preconditions: The argument cat is not null
        ContractEnforcerUtil.assertArgumentNonNull(type);
        String typeName = type.getName();
        assertIsNewType(typeName);
        catTypes.put(typeName, type);

        //Postconditions: Cat is now added to the Cache
        assert catTypes.get(typeName) != null;
    }

    protected void assertIsNewCat(CatId id) {
        //Preconditions: None, null check for id is done in hasCat
        if (hasCat(id)) {
            throw new IllegalStateException("Cat already exists!");
        }
        //Postconditions: None
    }

    protected void assertIsNewType(String typeName) {
        //Preconditions: None, null check for id is done in hasCat
        if (hasType(typeName)) {
            throw new IllegalStateException("CatType already exists!");
        }
        //Postconditions: None
    }

    //Documentation for Homework cw11 - object creation of Cat
    //Base call to CatManager.createCat() => call to CatManager.getType() => call to CatType.createInstance()
    public Cat createCat(String typeName){
        if(!hasType(typeName))return null;
        CatType type = getType(typeName);
        Cat result = type.createInstance();
        addCat(result);
        return result;
    }

    //No persistence is required, so this stays empty
    @Override
    protected Persistent createObject(ResultSet rset) throws SQLException {
        return null;
    }
}
