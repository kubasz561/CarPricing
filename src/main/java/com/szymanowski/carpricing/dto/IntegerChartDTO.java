package com.szymanowski.carpricing.dto;

import java.util.List;

public class IntegerChartDTO extends ChartDTO {
    private List<Integer> advertX;

    public List<Integer> getAdvertX() {
        return advertX;
    }

    public void setAdvertX(List<Integer> advertX) {
        this.advertX = advertX;
    }
}
