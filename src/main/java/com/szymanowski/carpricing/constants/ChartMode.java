package com.szymanowski.carpricing.constants;

/**
 * Typ wyliczeniowy rodzaju wykresu.
 */
public enum ChartMode {
    LINE("lines"),
    POINT("markers");

    private String value;

    public String getValue(){
        return value;
    }

    ChartMode(String value){
        this.value = value;
    }

}
