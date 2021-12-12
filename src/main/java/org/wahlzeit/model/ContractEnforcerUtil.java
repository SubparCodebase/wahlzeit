package org.wahlzeit.model;

import java.sql.SQLDataException;

public class ContractEnforcerUtil {

    public static void assertArgumentNonNull(Object... args){
        for(Object o : args){
            if(o == null)throw new IllegalArgumentException("null was passed as an Argument");
        }
    }

    //Ignore this
    /*public static void assertSQLDataNotMissing(Object actual, Object failureTarget, String errorMessage) throws SQLDataException {
        if(actual == null){
            if(failureTarget == null)throw new SQLDataException(errorMessage);
            return;
        }
        if(actual.equals(failureTarget))throw new SQLDataException(errorMessage);
    }*/
}
