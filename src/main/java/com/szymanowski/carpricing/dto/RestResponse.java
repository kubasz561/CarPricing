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

    public int getMedian() {
        return median;
    }

    public void setMedian(int median) {
        this.median = median;
    }

    private int median;

    public String getFiltersInfo() {
        return filtersInfo;
    }

    public void setFiltersInfo(String filtersInfo) {
        this.filtersInfo = filtersInfo;
    }

    private String filtersInfo;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String message;


}
