package com.szymanowski.carpricing.dto;

import com.szymanowski.carpricing.constants.ApproximationMethod;

public class CarData {

    private String make;
    private String model;
    private String version;

    private Integer year;
    private Integer mileage;

    //engine
    private Integer engineCapacity;
    private Integer power;
    private String fuel;

    private String type;//+year - optional
    private String color;//+year - optional
    private Boolean hadAccident;//+year - mandatory
    private Boolean isFirstOwner;//+year - mandatory

    private Boolean searchNewAdverts;

    private ApproximationMethod method;

    public CarData() {
    }


    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }


    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMileage() {
        return mileage;
    }

    public void setMileage(Integer mileage) {
        this.mileage = mileage;
    }

    public Integer getEngineCapacity() {
        return engineCapacity;
    }

    public void setEngineCapacity(Integer engineCapacity) {
        this.engineCapacity = engineCapacity;
    }

    public Integer getPower() {
        return power;
    }

    public void setPower(Integer power) {
        this.power = power;
    }

    public String getFuel() {
        return fuel;
    }

    public void setFuel(String fuel) {
        this.fuel = fuel;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Boolean getSearchNewAdverts() {
        return searchNewAdverts;
    }

    public void setSearchNewAdverts(Boolean searchNewAdverts) {
        this.searchNewAdverts = searchNewAdverts;
    }

    public Boolean getHadAccident() {
        return hadAccident;
    }

    public void setHadAccident(Boolean hadAccident) {
        this.hadAccident = hadAccident;
    }

    public Boolean getIsFirstOwner() {
        return isFirstOwner;
    }

    public void setIsFirstOwner(Boolean isFirstOwner) {
        this.isFirstOwner = isFirstOwner;
    }

    public ApproximationMethod getMethod() {
        return method;
    }

    public void setMethod(ApproximationMethod method) {
        this.method = method;
    }


    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }


}
