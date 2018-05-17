package com.szymanowski.carpricing.dto;

import java.util.List;

public class RestResponse {

    public LPResultDTO getLpResultDTO() {
        return lpResultDTO;
    }

    public void setLpResultDTO(LPResultDTO lpResultDTO) {
        this.lpResultDTO = lpResultDTO;
    }

    public int getFormPrice() {
        return formPrice;
    }

    public void setFormPrice(int formPrice) {
        this.formPrice = formPrice;
    }

    public int getAverageDiff() {
        return averageDiff;
    }

    public void setAverageDiff(int averageDiff) {
        this.averageDiff = averageDiff;
    }

    public List<ChartDTO> getCharts() {
        return charts;
    }

    public void setCharts(List<ChartDTO> charts) {
        this.charts = charts;
    }

    private List<ChartDTO> charts;
    private LPResultDTO lpResultDTO;
    private int formPrice;
    private int averageDiff;


}
