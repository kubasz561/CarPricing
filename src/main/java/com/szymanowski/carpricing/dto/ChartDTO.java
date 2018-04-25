package com.szymanowski.carpricing.dto;



public class ChartDTO{

    private String type;


    private int[] advertX;
    private int[] advertY;
    private double[] regressY;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int[] getAdvertX() {
        return advertX;
    }

    public void setAdvertX(int[] advertX) {
        this.advertX = advertX;
    }

    public int[] getAdvertY() {
        return advertY;
    }

    public void setAdvertY(int[] advertY) {
        this.advertY = advertY;
    }

    public double[] getRegressY() {
        return regressY;
    }

    public void setRegressY(double[] regressY) {
        this.regressY = regressY;
    }
}