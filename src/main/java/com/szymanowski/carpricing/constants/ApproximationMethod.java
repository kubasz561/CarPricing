package com.szymanowski.carpricing.constants;

/**
 * Typ wyliczeniowy rodzaju wyceny
 */
public enum ApproximationMethod {
    WEIGHTED_MEAN("WEIGHTED_MEAN"),
    MAX_PRICE("MAX_PRICE");

    private String value;

    public String getValue(){
        return value;
    }

    ApproximationMethod(String value){
        this.value = value;
    }
}
