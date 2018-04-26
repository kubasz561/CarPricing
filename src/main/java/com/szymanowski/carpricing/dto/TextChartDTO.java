package com.szymanowski.carpricing.dto;

import java.util.List;

public class TextChartDTO extends ChartDTO{


    private List<String> advertX;

    public List<String> getAdvertX() {
        return advertX;
    }

    public void setAdvertX(List<String> advertX) {
        this.advertX = advertX;
    }

}
