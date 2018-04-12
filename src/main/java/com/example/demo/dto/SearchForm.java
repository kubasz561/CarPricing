package com.example.demo.dto;

public class SearchForm {
    public String getMarka() {
        return marka;
    }

    public SearchForm() {
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

    private String marka;
    private String model;

}
