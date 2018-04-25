package com.szymanowski.carpricing.services;

import com.szymanowski.carpricing.dto.ChartDTO;
import com.szymanowski.carpricing.repository.Adverts;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ApproximationService {
    public List<ChartDTO> approximate(List<Adverts> adverts){
        List<ChartDTO> charts = new ArrayList<>();

        charts.add(mileageRegression(adverts));
        charts.add(yearRegression(adverts));
        charts.add(yearMileageRegression(adverts));

        return charts;
    }

    private ChartDTO mileageRegression(List<Adverts> adverts) {
        SimpleRegression regression = new SimpleRegression();
        int[] advertX = new int[adverts.size()];
        int[] advertY = new int[adverts.size()];
        double[] regressY = new double[adverts.size()];


        adverts.forEach(advert -> {
            regression.addData(advert.getMileage(), advert.getPrice());
        });

        for(int i = 0; i < adverts.size(); i ++){
            advertX[i] = adverts.get(i).getMileage();
            advertY[i] = adverts.get(i).getPrice();
            regressY[i] = regression.predict(advertX[i]);
        }

        ChartDTO chart = new ChartDTO();
        chart.setType("Mileage");
        chart.setAdvertX(advertX);
        chart.setAdvertY(advertY);
        chart.setRegressY(regressY);
        return chart;
    }

    private ChartDTO yearRegression(List<Adverts> adverts) {
        SimpleRegression regression = new SimpleRegression();
        int[] advertX = new int[adverts.size()];
        int[] advertY = new int[adverts.size()];
        double[] regressY = new double[adverts.size()];


        adverts.forEach(advert -> {
            regression.addData(advert.getYear(), advert.getPrice());
        });

        for(int i = 0; i < adverts.size(); i ++){
            advertX[i] = adverts.get(i).getYear();
            advertY[i] = adverts.get(i).getPrice();
            regressY[i] = regression.predict(advertX[i]);
        }

        ChartDTO chart = new ChartDTO();
        chart.setType("Year");
        chart.setAdvertX(advertX);
        chart.setAdvertY(advertY);
        chart.setRegressY(regressY);
        return chart;
    }

    //Polynomial test
    private ChartDTO yearMileageRegression(List<Adverts> adverts) {
        PolynomialCurveFitter regression = PolynomialCurveFitter.create(3);
        int[] advertX = new int[adverts.size()];
        int[] advertY = new int[adverts.size()];
        double[] regressY = new double[adverts.size()];

         WeightedObservedPoints obs = new WeightedObservedPoints();

        adverts.forEach(advert -> {
            obs.add(advert.getYear(), advert.getMileage());
        });

        PolynomialFunction polynomialFunction = new PolynomialFunction(regression.fit(obs.toList()));

        for(int i = 0; i < adverts.size(); i ++){
            advertX[i] = adverts.get(i).getYear();
            advertY[i] = adverts.get(i).getMileage();
            regressY[i] = polynomialFunction.value(adverts.get(i).getYear());
        }
        ChartDTO chart = new ChartDTO();
        chart.setType("Year-Mileage");
        chart.setAdvertX(advertX);
        chart.setAdvertY(advertY);
        chart.setRegressY(regressY);
        return chart;
    }




}
