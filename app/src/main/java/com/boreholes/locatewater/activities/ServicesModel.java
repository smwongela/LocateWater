package com.boreholes.locatewater.activities;

public class ServicesModel {

    //declare the variable
    private String title, desc, cost, serviceImage;
    //create a constructor

    public ServicesModel(String title, String desc, String cost, String serviceImages) {
        this.title = title;
        this.desc = desc;
        this.cost = cost;
        this.serviceImage =serviceImage;

    }
    //requires an empty constructor
    public ServicesModel() {
    }

    // setters



    public void setTitle(String title) {
        this.title = title;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }
    public void setServiceImage(String serviceImage) {
        this.serviceImage = serviceImage;
    }

    //getters



    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public String getCost() {
        return cost;
    }
    public String getServiceImage() {
        return serviceImage;
    }





}


