package com.boreholes.locatewater.activities;


public class EventModel {
    private String  sourcePhoto,sourceName,sourceCounty, sourceSubCounty,sourceAddress;
    //create a constructor

    public EventModel( String sourcePhoto,String sourceName, String sourceCounty, String sourceSubCounty, String sourceAddress) {

       this.sourcePhoto = sourcePhoto;
       this.sourceName =sourceName;
       this.sourceCounty = sourceCounty;
       this.sourceSubCounty = sourceSubCounty;
       this.sourceAddress =sourceAddress;

    }
    //requires an empty constructor
    public EventModel() {
    }
    // setters
    public void setSourcePhoto(String sourcePhoto){
        this.sourcePhoto =sourcePhoto;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }
    public void setSourceCounty(String sourceCounty){
        this.sourceCounty=sourceCounty;
    }
    public void setSourceSubCounty(String sourceSubCounty){
        this.sourceSubCounty=sourceSubCounty;
    }
    public void setSourceAddress(String sourceAddress){
        this.sourceAddress=sourceAddress;
    }


    //getters

    public String getSourcePhoto()
    {
        return sourcePhoto;
    }
    public String getSourceName() {
        return sourceName;
    }
    public String getSourceCounty(){
        return sourceCounty;
    }
    public String getSourceSubCounty(){
        return sourceSubCounty;
    }
    public String getSourceAddress(){
        return sourceAddress;
    }



}