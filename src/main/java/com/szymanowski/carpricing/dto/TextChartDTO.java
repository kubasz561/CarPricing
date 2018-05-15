package com.szymanowski.carpricing.dto;

import java.util.List;

public class TextChartDTO extends ChartDTO{


    private List<String> advertX;

    public List<String> getRegressX() {
        return regressX;
    }

    public void setRegressX(List<String> regressX) {
        this.regressX = regressX;
    }

    private List<String> regressX;

    public String getFormX() {
        return formX;
    }

    public void setFormX(String formX) {
        this.formX = formX;
    }

    private String formX;

    public List<String> getAdvertX() {
        return advertX;
    }

    public void setAdvertX(List<String> advertX) {
        this.advertX = advertX;
    }

}
