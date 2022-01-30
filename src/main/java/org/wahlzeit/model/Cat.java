package org.wahlzeit.model;

import java.util.Date;

public class Cat {

    //Client-Service-Collaboration with CatPhoto; Bound role: Service
    //Type-Object-Collaboration with CatType; Bound role: Base Object
    //Manager-Collaboration with CatManager; Bound role: Element
    //final since a cat should not change type (hopefully)
    protected final CatType type;
    protected CatId id = null;
    //Additional attributes for flavor
    protected Date birthDate;
    //Length in cm
    protected float length;

    //Documentation for Homework cw11 - object creation of Cat
    //Call to Cat()
    public Cat(CatType ct){
        type = ct;
        id = CatId.getNextId();
    }

    public Cat(CatType ct, CatId id){
        type = ct;
        this.id = id;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public float getLength() {
        return length;
    }

    public CatType getType() {
        return type;
    }

    public CatId getId() {
        return id;
    }
}
