package com.szymanowski.carpricing.dto;


/**
 * Przechowuje dane zwracane w odpowiedzi serwera na metodę search, czyli wszystkie dane dotyczące wyceny.
 */
public class MPResultDTO {
    private double totalDiff;
    private int filteredAdvertsCount;
    private double[] wParams;

    public int getFilteredAdvertsCount() { //front
        return filteredAdvertsCount;
    }

    public void setFilteredAdvertsCount(int filteredAdvertsCount) {
        this.filteredAdvertsCount = filteredAdvertsCount;
    }

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

}
