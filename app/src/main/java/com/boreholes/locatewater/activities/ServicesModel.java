package com.boreholes.locatewater.activities;

public class ServicesModel {

    //declare the variable
    private String location, videoTitle, county,subCounty,ward;
    //create a constructor

    public ServicesModel(String location, String videoTitle, String county, String subCounty, String ward) {
        this.location = location;
        this.videoTitle = videoTitle;
        this.county = county;
        this.subCounty =subCounty;
        this.ward = ward;

    }
    //requires an empty constructor
    public ServicesModel() {
    }

    // setters



    public void setLocation(String location) {
        this.location = location;
    }

   public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public void setCounty(String county) {
        this.county = county;
    }
    public void setSubCounty(String subCounty) {
        this.subCounty = subCounty;
    }
    public  void setWard(String ward){this.ward=ward;}




    //getters



    public String getLocation() {
        return location;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public String getCounty() {
        return county;
    }
    public String getSubCounty() {
        return subCounty;
    }
    public String getWard(){
        return ward;
    }





}


