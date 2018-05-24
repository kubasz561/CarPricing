package com.szymanowski.carpricing.repository;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class MakeModel {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    private String make;
    private String model;
    private String version;
    private String engineCode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getEngineCode() {
        return engineCode;
    }

    public void setEngineCode(String engineCode) {
        this.engineCode = engineCode;
    }
}

