package com.szymanowski.carpricing.dto;


public class LPResultDTO {
    private double totalDiff;

    public int getFilteredAdvertsCount() {
        return filteredAdvertsCount;
    }

    public void setFilteredAdvertsCount(int filteredAdvertsCount) {
        this.filteredAdvertsCount = filteredAdvertsCount;
    }

    private int filteredAdvertsCount;

    public double getTotalDiff() {
        return totalDiff;
    }

    public void setTotalDiff(double totalDiff) {
        this.totalDiff = totalDiff;
    }

    public double[] getwParams() {
        return wParams;
    }

    public void setwParams(double[] wParams) {
        this.wParams = wParams;
    }

    private double[] wParams;

}
