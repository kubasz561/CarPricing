package com.szymanowski.carpricing.repository;

import javax.persistence.*;
import java.util.Date;

/**
 * Klasa reprezentująca tabelę Adverts. Tabela zawiera ogłoszenia znalezione w internecie
 */
@Entity
public class Adverts {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    private Long advertId;
    @Temporal(TemporalType.DATE)
    private Date saveDate;

    private String name;
    private Integer price;

    private String make;
    private String model;

    private String version;

    private Integer year;

    private Integer mileage;

    private Integer engineCapacity;
    private Integer power;
    private String fuel;

    private String type;
    private String color;
    private Boolean hadAccident;
    private Boolean isFirstOwner;


    public Long getAdvertId() {
        return advertId;
    }

    public void setAdvertId(Long advertId) {
        this.advertId = advertId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
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

    public Boolean getHadAccident() {
        return hadAccident;
    }

    public void setHadAccident(Boolean hadAccident) {
        this.hadAccident = hadAccident;
    }

    public Boolean getFirstOwner() {
        return isFirstOwner;
    }

    public void setFirstOwner(Boolean firstOwner) {
        isFirstOwner = firstOwner;
    }


    public Date getSaveDate() {
        return saveDate;
    }

    public void setSaveDate(Date saveDate) {
        this.saveDate = saveDate;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

}
