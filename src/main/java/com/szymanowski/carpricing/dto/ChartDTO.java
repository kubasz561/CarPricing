package com.szymanowski.carpricing.dto;


import java.util.List;

public class ChartDTO {

    private String type;
    private String mainChartMode;
    private String approxChartMode;
    private List<Integer> advertY;
    private List<Double> regressY;
    private Double formY;

    public String getMainChartMode() {
        return mainChartMode;
    }

    public void setMainChartMode(String mainChartMode) {
        this.mainChartMode = mainChartMode;
    }

    public String getApproxChartMode() {
        return approxChartMode;
    }

    public void setApproxChartMode(String approxChartMode) {
        this.approxChartMode = approxChartMode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public List<Integer> getAdvertY() {
        return advertY;
    }

    public void setAdvertY(List<Integer> advertY) {
        this.advertY = advertY;
    }

    public List<Double> getRegressY() {
        return regressY;
    }

    public void setRegressY(List<Double> regressY) {
        this.regressY = regressY;
    }

    public Double getFormY() {
        return formY;
    }

    public void setFormY(Double formY) {
        this.formY = formY;
    }

}