package com.szymanowski.carpricing.dto;

import java.util.List;

public class IntegerChartDTO extends ChartDTO {
    private List<Integer> advertX;
    private Integer formX;
    private List<Integer> regressX;
    private double r;


    public double getR() {
        return r;
    }
    public void setR(double r) {
        this.r = r;
    }

    public Integer getFormX() {
        return formX;
    }

    public void setFormX(Integer formX) {
        this.formX = formX;
    }

    public List<Integer> getRegressX() {
        return regressX;
    }

    public void setRegressX(List<Integer> regressX) {
        this.regressX = regressX;
    }

    public List<Integer> getAdvertX() {
        return advertX;
    }

    public void setAdvertX(List<Integer> advertX) {
        this.advertX = advertX;
    }
}
