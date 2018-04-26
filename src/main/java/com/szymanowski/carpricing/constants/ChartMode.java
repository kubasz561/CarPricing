package com.szymanowski.carpricing.constants;

public enum ChartMode {
    LINE("lines"),
    POINT("markers");

    private String value;

    public String getValue(){
        return value;
    }

    private ChartMode(String value){
        this.value = value;
    }

}
