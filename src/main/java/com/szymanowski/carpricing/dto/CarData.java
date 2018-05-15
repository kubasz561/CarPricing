package com.szymanowski.carpricing.dto;

public class CarData {

    private String marka;
    private String model;

    private String year;
    private Integer mileage;

    //engine
    private Integer engineCapacity;
    private Integer power;
    private String fuel;

    private String type;//+year - optional
    private String color;//+year - optional
    private Boolean hadAccident;//+year - mandatory
    private Boolean isFirstOwner;//+year - mandatory

    private Boolean isNew;

    private String description;

    public CarData() {
    }


    public String getMarka() {
        return marka;
    }

    public void setMarka(String marka) {
        this.marka = marka;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }


    public String getYear() {
        return year;
    }

    public void setYear(String year) {
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

    public Boolean getIsNew() {
        return isNew;
    }

    public void setIsNew(Boolean isNew) {
        this.isNew = isNew;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
