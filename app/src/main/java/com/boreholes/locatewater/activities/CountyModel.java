package com.boreholes.locatewater.activities;

public class CountyModel {
    private String  countyName;

    public CountyModel( String countyName) {

        this.countyName = countyName;

    }
    //requires an empty constructor
    public CountyModel() {
    }
    // setters
    public void setCountyName(String countyName){
        this.countyName =countyName;
    }
    //getters

    public String getCountyName()
    {
        return countyName;
    }



}

