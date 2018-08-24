package com.szymanowski.carpricing.dto;

import java.util.List;

public class IntegerChartDTO extends ChartDTO {
    public double getR() {
        return r;
    }

    public void setR(double r) {
        this.r = r;
    }

    private double r;

    private List<Integer> advertX;

    public Integer getFormX() {
        return formX;
    }

    public void setFormX(Integer formX) {
        this.formX = formX;
    }

    private Integer formX;

    public List<Integer> getRegressX() {
        return regressX;
    }

    public void setRegressX(List<Integer> regressX) {
        this.regressX = regressX;
    }

    private List<Integer> regressX;

    public List<Integer> getAdvertX() {
        return advertX;
    }

    public void setAdvertX(List<Integer> advertX) {
        this.advertX = advertX;
    }
}
