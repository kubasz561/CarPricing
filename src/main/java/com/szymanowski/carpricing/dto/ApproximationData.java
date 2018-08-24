package com.szymanowski.carpricing.dto;

import com.szymanowski.carpricing.services.ParametersInfo;

import java.util.List;

public class ApproximationData {
    private List<ChartDTO> charts;
    private ParametersInfo parametersInfo;

    public ApproximationData(List<ChartDTO> charts, ParametersInfo parametersInfo){
        this.charts = charts;
        this.parametersInfo = parametersInfo;
    }

    public List<ChartDTO> getCharts() {
        return charts;
    }

    public void setCharts(List<ChartDTO> charts) {
        this.charts = charts;
    }

    public ParametersInfo getParametersInfo() {
        return parametersInfo;
    }

    public void setParametersInfo(ParametersInfo parametersInfo) {
        this.parametersInfo = parametersInfo;
    }
}
