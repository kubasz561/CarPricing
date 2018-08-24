package com.szymanowski.carpricing.dto;

import java.util.List;

public class TextChartDTO extends ChartDTO{


    private List<String> advertX;
    private String formX;
    private List<String> regressX;

    public List<String> getRegressX() {
        return regressX;
    }

    public void setRegressX(List<String> regressX) {
        this.regressX = regressX;
    }

    public String getFormX() {
        return formX;
    }

    public void setFormX(String formX) {
        this.formX = formX;
    }

    public List<String> getAdvertX() {
        return advertX;
    }

    public void setAdvertX(List<String> advertX) {
        this.advertX = advertX;
    }

}
