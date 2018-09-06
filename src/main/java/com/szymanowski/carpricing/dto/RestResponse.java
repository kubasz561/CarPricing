package com.szymanowski.carpricing.dto;

import java.util.List;

/**
 * Przechowuje dane zwracane w odpowiedzi serwera na metodę search, czyli wszystkie dane dotyczące wyceny.
 */
public class RestResponse {

    private List<ChartDTO> charts;
    private int advertsCount;
    private int formPrice;
    private int averageDiff;
    private int diffPercent;
    private int median;
    private String filtersInfo;
    private String message;
    public int getAdvertsCount() {
        return advertsCount;
    }

    public void setAdvertsCount(int advertsCount) {
        this.advertsCount = advertsCount;
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

    public int getDiffPercent() {
        return diffPercent;
    }

    public void setDiffPercent(int diffPercent) {
        this.diffPercent = diffPercent;
    }

    public List<ChartDTO> getCharts() {
        return charts;
    }

    public void setCharts(List<ChartDTO> charts) {
        this.charts = charts;
    }

    public int getMedian() {
        return median;
    }

    public void setMedian(int median) {
        this.median = median;
    }

    public String getFiltersInfo() {
        return filtersInfo;
    }

    public void setFiltersInfo(String filtersInfo) {
        this.filtersInfo = filtersInfo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
