package com.szymanowski.carpricing.constants;

public enum Params {
    MILEAGE("Przebieg"),
    YEAR("Rok"),
    ENGINE("Silnik"),
    COLOR("Kolor"),
    TYPE("Typ"),
    ACCIDENT_YEAR("Czy miał wypadek - rocznie"),
    FIRST_OWNER_YEAR("Pierwszy właściciel - rocznie");

    private String value;

    public String getValue(){
        return value;
    }

    Params(String value){
        this.value = value;
    }
}
