package com.szymanowski.carpricing.dto;

import java.util.List;

public class IntegerChartDTO extends ChartDTO {
    public double getR() {
        return r;
    }

    public void setR(double r) {
        this.r = r;
    }

    public double getR2() {
        return r2;
    }

    public void setR2(double r2) {
        this.r2 = r2;
    }

    public double getSignificance() {
        return significance;
    }

    public void setSignificance(double significance) {
        this.significance = significance;
    }

    private double r;
    private double r2;
    private double significance;
    private List<Integer> advertX;

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
