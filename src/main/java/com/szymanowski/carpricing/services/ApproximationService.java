package com.szymanowski.carpricing.services;

import com.szymanowski.carpricing.constants.ChartMode;
import com.szymanowski.carpricing.dto.ChartDTO;
import com.szymanowski.carpricing.dto.IntegerChartDTO;
import com.szymanowski.carpricing.dto.TextChartDTO;
import com.szymanowski.carpricing.repository.Adverts;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ApproximationService {
    public List<ChartDTO> approximate(List<Adverts> adverts){
        List<ChartDTO> charts = new ArrayList<>();

        charts.add(mileageRegression(adverts));
        charts.add(yearRegression(adverts));
        charts.add(colorMean(adverts));

        return charts;
    }

    private ChartDTO mileageRegression(List<Adverts> adverts) {
        SimpleRegression regression = new SimpleRegression();
        List<Integer> advertX = new ArrayList<>();
        List<Integer> advertY = new ArrayList<>();
        List<Double> regressY = new ArrayList<>();

        adverts.stream()
                .filter(advert -> advert.getMileage() != null)
                .forEach(advert ->
                    regression.addData(advert.getMileage(), advert.getPrice())
                );

        adverts.stream()
                .filter(advert -> advert.getMileage() != null)
                .forEach(advert -> {
                    advertX.add(advert.getMileage());
                    advertY.add(advert.getPrice());
                    regressY.add(regression.predict(advert.getMileage()));
                });

        IntegerChartDTO chart = new IntegerChartDTO();
        chart.setType("Mileage");
        chart.setAdvertX(advertX);
        chart.setAdvertY(advertY);
        chart.setRegressY(regressY);
        chart.setMainChartMode(ChartMode.POINT.getValue());
        chart.setApproxChartMode(ChartMode.LINE.getValue());
        return chart;
    }

    private ChartDTO yearRegression(List<Adverts> adverts) {
        SimpleRegression regression = new SimpleRegression();
        List<Integer> advertX = new ArrayList<>();
        List<Integer> advertY = new ArrayList<>();
        List<Double> regressY = new ArrayList<>();

        adverts.stream()
                .filter(advert -> advert.getYear() != null)
                .forEach(advert ->
                        regression.addData(advert.getYear(), advert.getPrice())
                );

        adverts.stream()
                .filter(advert -> advert.getYear() != null)
                .forEach(advert -> {
                    advertX.add(advert.getYear());
                    advertY.add(advert.getPrice());
                    regressY.add(regression.predict(advert.getYear()));
                });

        IntegerChartDTO chart = new IntegerChartDTO();
        chart.setType("Year");
        chart.setAdvertX(advertX);
        chart.setAdvertY(advertY);
        chart.setRegressY(regressY);
        chart.setMainChartMode(ChartMode.POINT.getValue());
        chart.setApproxChartMode(ChartMode.LINE.getValue());
        return chart;
    }

    private ChartDTO colorMean(List<Adverts> adverts){
        List<String> advertX = new ArrayList<>();
        List<Integer> advertY = new ArrayList<>();
        List<Double> regressY = new ArrayList<>();


        MultiValueMap<String, Integer> colorPriceMap = new LinkedMultiValueMap<>();

        adverts.stream()
                .filter(advert -> advert.getColor() != null)
                .forEach(advert -> {
                    colorPriceMap.add(advert.getColor(), advert.getPrice());
                    advertX.add(advert.getColor());
                    advertY.add(advert.getPrice());
                });
        colorPriceMap.add("czarny",5);
        colorPriceMap.add("czarny",7);
        colorPriceMap.add("czarny",2);
        colorPriceMap.add("czarny",34);
        colorPriceMap.add("biały",34);
        colorPriceMap.add("biały",334);
        colorPriceMap.add("biały",3);
        colorPriceMap.add("czerwony",46);

        Map<String, Double> means = new HashMap<>();

        colorPriceMap.keySet().forEach( key -> {
            List<Integer> values = colorPriceMap.get(key);
            Double mean = (double) values.stream().mapToInt(Integer::intValue).sum() / values.size();
            means.put(key,mean);
        });

        adverts.stream()
                .filter(advert -> advert.getColor() != null)
                .forEach(advert -> {
                    advertX.add(advert.getColor());
                    advertY.add(advert.getPrice());
                    regressY.add(means.get(advert.getColor()));
                });

        TextChartDTO chart = new TextChartDTO();
        chart.setType("Color");
        chart.setAdvertX(advertX);
        chart.setAdvertY(advertY);
        chart.setRegressY(regressY);
        chart.setMainChartMode(ChartMode.POINT.getValue());
        chart.setApproxChartMode(ChartMode.POINT.getValue());

        return chart;
    }


 /*   //Polynomial test
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
    }*/



}
