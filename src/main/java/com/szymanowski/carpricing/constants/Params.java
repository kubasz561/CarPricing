package com.szymanowski.carpricing.constants;

public enum Params {
    MILEAGE("Mileage"),
    YEAR("Year"),
    ENGINE("Engine"),
    COLOR("Color"),
    ACCIDENT_YEAR("Bezwypadkowy"),
    COLOR_YEAR("Color - Year"),
    FIRST_OWNER_YEAR("First Owner - Year"),
    TYPE("Type"),
    TYPE_YEAR("Type - Year");

    private String value;

    public String getValue(){
        return value;
    }

    private Params(String value){
        this.value = value;
    }
}
