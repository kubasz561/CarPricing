package com.szymanowski.carpricing.constants;

public enum Params {
    MILEAGE("Przebieg"),
    YEAR("Rok"),
    ENGINE("Silnik"),
    COLOR("Kolor"),
    ACCIDENT_YEAR("Czy miał wypadek - rocznie"),
    COLOR_YEAR("Color - Year"),
    FIRST_OWNER_YEAR("Pierwszy właściciel - rocznie"),
    TYPE("Typ"),
    TYPE_YEAR("Type - Year");

    private String value;

    public String getValue(){
        return value;
    }

    Params(String value){
        this.value = value;
    }
}
