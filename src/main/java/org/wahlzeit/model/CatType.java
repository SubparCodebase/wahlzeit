package org.wahlzeit.model;

import java.util.LinkedList;

public class CatType {

    protected String name;
    protected CatType superType = null;
    protected LinkedList<CatType> subTypes = new LinkedList<CatType>();

    public CatType(String name){
        this.name = name;
        CatManager.getInstance().addType(this);
    }

    public CatType(String name, CatType superType){
        ContractEnforcerUtil.assertArgumentNonNull(superType);
        this.name = name;
        superType.addSubType(this);
        CatManager.getInstance().addType(this);
    }

    //Documentation for Homework cw11 - object creation of Cat
    //Call to CatType.createInstance() => call to Cat()
    public Cat createInstance(){
        return new Cat(this);
    }

    public String getName() {
        return name;
    }

    public CatType getSuperType() {
        return superType;
    }

    private void setSuperType(CatType superType) {
        this.superType = superType;
    }

    public LinkedList<CatType> getSubTypes() {
        return subTypes;
    }

    public void addSubType(CatType type){
        ContractEnforcerUtil.assertArgumentNonNull(type);
        type.setSuperType(this);
        subTypes.add(type);
    }

    //Checking from the bottom up, to circumvent a depth-first-search
    public boolean isSubType(CatType type){
        if(type == null)return false;
        if(superType == type)return true;
        if(superType == null)return false;
        return this.superType.isSubType(type);
    }

    //Reusing previous code
    public boolean isSuperType(CatType type){
        return type.isSubType(this);
    }

    //Also checking from the bottom up
    public boolean hasInstance(Cat cat){
        ContractEnforcerUtil.assertArgumentNonNull(cat);
        if(cat.getType() == this)return true;
        return cat.getType().isSubType(this);
    }
}
