package com.szymanowski.carpricing.constants;

public enum ApproximationMethod {
    MATH_PROGRAMMING("MP"),
    MAX_PRICE("MAX");

    private String value;

    public String getValue(){
        return value;
    }

    ApproximationMethod(String value){
        this.value = value;
    }
}
