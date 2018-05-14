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
import java.util.*;

@Service
public class ApproximationService {
    public List<ChartDTO> approximate(List<Adverts> adverts){
        List<ChartDTO> charts = new ArrayList<>();

        charts.add(mileageRegression(adverts));
       // charts.add(mileageNoAccidentRegression(adverts));
        charts.add(yearRegression(adverts));
        charts.add(powerRegression(adverts));
        charts.add(colorMean(adverts));
        charts.add(fuelMean(adverts));
        charts.add(powerMean(adverts));
        charts.add(capacityMean(adverts));
        charts.add(yearMileageRegression(adverts));

        return charts;
    }

    private ChartDTO mileageRegression(List<Adverts> adverts) {
        SimpleRegression regression = new SimpleRegression();
        List<Integer> advertX = new ArrayList<>();
        List<Integer> advertY = new ArrayList<>();
        List<Integer> regressX = new ArrayList<>();
        List<Double> regressY = new ArrayList<>();

        adverts.stream()
                .filter(advert -> advert.getMileage() != null)
                .forEach(advert -> {
                    regression.addData(advert.getMileage(), advert.getPrice());
                    advertX.add(advert.getMileage());
                    advertY.add(advert.getPrice());
                });
        getRegressionPoints(advertX,regressX,regressY, regression);
        return createIntegerChartData("Mileage", advertX, advertY, regressX, regressY, regression);
    }

    private ChartDTO mileageNoAccidentRegression(List<Adverts> adverts) {
        SimpleRegression regression = new SimpleRegression();
        List<Integer> advertX = new ArrayList<>();
        List<Integer> advertY = new ArrayList<>();
        List<Integer> regressX = new ArrayList<>();
        List<Double> regressY = new ArrayList<>();

        adverts.stream()
                .filter(advert -> advert.getMileage() != null)
                .filter(advert -> Boolean.FALSE.equals(advert.getHadAccident()))
                .forEach(advert -> {
                    regression.addData(advert.getMileage(), advert.getPrice());
                    advertX.add(advert.getMileage());
                    advertY.add(advert.getPrice());
                });

        getRegressionPoints(advertX,regressX,regressY, regression);
        return createIntegerChartData("Mileage no Accident", advertX, advertY, regressX, regressY);
    }

    private ChartDTO yearRegression(List<Adverts> adverts) {
        SimpleRegression regression = new SimpleRegression();
        List<Integer> advertX = new ArrayList<>();
        List<Integer> advertY = new ArrayList<>();
        List<Integer> regressX = new ArrayList<>();
        List<Double> regressY = new ArrayList<>();

        adverts.stream()
                .filter(advert -> advert.getYear() != null)
                .forEach(advert -> {
                    regression.addData(advert.getYear(), advert.getPrice());
                    advertX.add(advert.getYear());
                    advertY.add(advert.getPrice());
                });

        getRegressionPoints(advertX,regressX,regressY, regression);
        return createIntegerChartData("Year", advertX, advertY, regressX, regressY);
    }
    private ChartDTO powerRegression(List<Adverts> adverts) {
        SimpleRegression regression = new SimpleRegression();
        List<Integer> advertX = new ArrayList<>();
        List<Integer> advertY = new ArrayList<>();
        List<Integer> regressX = new ArrayList<>();
        List<Double> regressY = new ArrayList<>();

        adverts.stream()
                .filter(advert -> advert.getPower() != null)
                .forEach(advert -> {
                    regression.addData(advert.getPower(), advert.getPrice());
                    advertX.add(advert.getPower());
                    advertY.add(advert.getPrice());
                });

        getRegressionPoints(advertX,regressX,regressY, regression);
        return createIntegerChartData("Power KM - regress", advertX, advertY, regressX, regressY);
    }

    private ChartDTO colorMean(List<Adverts> adverts){
        List<String> advertX = new ArrayList<>();
        List<Integer> advertY = new ArrayList<>();
        List<String> regressX = new ArrayList<>();
        List<Double> regressY = new ArrayList<>();
        MultiValueMap<String, Integer> colorPriceMap = new LinkedMultiValueMap<>();
        Map<String, Double> means = new HashMap<>();

        adverts.stream()
                .filter(advert -> advert.getColor() != null)
                .forEach(advert -> {
                    colorPriceMap.add(advert.getColor(), advert.getPrice());
                    advertX.add(advert.getColor());
                    advertY.add(advert.getPrice());
                });

        getMeanPoints(regressX, regressY, colorPriceMap, means);
        return createTextChartData("Color",advertX, advertY, regressX, regressY);
    }



    private ChartDTO fuelMean(List<Adverts> adverts){
        List<String> advertX = new ArrayList<>();
        List<Integer> advertY = new ArrayList<>();
        List<String> regressX = new ArrayList<>();
        List<Double> regressY = new ArrayList<>();
        MultiValueMap<String, Integer> colorPriceMap = new LinkedMultiValueMap<>();
        Map<String, Double> means = new HashMap<>();

        adverts.stream()
                .filter(advert -> advert.getFuel() != null)
                .forEach(advert -> {
                    colorPriceMap.add(advert.getFuel(), advert.getPrice());
                    advertX.add(advert.getFuel());
                    advertY.add(advert.getPrice());
                });

        getMeanPoints(regressX, regressY, colorPriceMap, means);
        return createTextChartData("Fuel",advertX, advertY, regressX, regressY);
    }
    private ChartDTO powerMean(List<Adverts> adverts){
        List<String> advertX = new ArrayList<>();
        List<Integer> advertY = new ArrayList<>();
        List<String> regressX = new ArrayList<>();
        List<Double> regressY = new ArrayList<>();
        MultiValueMap<String, Integer> colorPriceMap = new LinkedMultiValueMap<>();
        Map<String, Double> means = new HashMap<>();

        adverts.stream()
                .filter(advert -> advert.getPower() != null)
                .forEach(advert -> {
                    colorPriceMap.add(advert.getPower().toString(), advert.getPrice());
                    advertX.add(advert.getPower().toString());
                    advertY.add(advert.getPrice());
                });

        getMeanPoints(regressX, regressY, colorPriceMap, means);
        return createTextChartData("Power KM - Mean",advertX, advertY, regressX, regressY);
    }
    private ChartDTO capacityMean(List<Adverts> adverts){
        List<String> advertX = new ArrayList<>();
        List<Integer> advertY = new ArrayList<>();
        List<String> regressX = new ArrayList<>();
        List<Double> regressY = new ArrayList<>();
        MultiValueMap<String, Integer> colorPriceMap = new LinkedMultiValueMap<>();
        Map<String, Double> means = new HashMap<>();

        adverts.stream()
                .filter(advert -> advert.getEngineCapacity() != null)
                .forEach(advert -> {
                    colorPriceMap.add(advert.getEngineCapacity().toString(), advert.getPrice());
                    advertX.add(advert.getEngineCapacity().toString());
                    advertY.add(advert.getPrice());
                });

        getMeanPoints(regressX, regressY, colorPriceMap, means);
        return createTextChartData("Capacity",advertX, advertY, regressX, regressY);
    }

    private ChartDTO yearMileageRegression(List<Adverts> adverts) {
        SimpleRegression regression = new SimpleRegression();
        List<Integer> advertX = new ArrayList<>();
        List<Integer> advertY = new ArrayList<>();
        List<Integer> regressX = new ArrayList<>();
        List<Double> regressY = new ArrayList<>();

        adverts.stream()
                .filter(advert -> advert.getYear() != null)
                .forEach(advert -> {
                    regression.addData(advert.getYear(), advert.getMileage());
                    advertX.add(advert.getYear());
                    advertY.add(advert.getMileage());
                });

        getRegressionPoints(advertX, regressX, regressY, regression);
        return createIntegerChartData("Year", advertX, advertY, regressX, regressY);
    }

 /*=   //Polynomial test
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




    private void getMeanPoints(List<String> regressX, List<Double> regressY, MultiValueMap<String, Integer> colorPriceMap, Map<String, Double> means) {
        colorPriceMap.keySet().forEach( key -> {
            List<Integer> values = colorPriceMap.get(key);
            Double mean = (double) values.stream().mapToInt(Integer::intValue).sum() / values.size();
            means.put(key,mean);
            regressX.add(key);
            regressY.add(mean);
        });
    }

    private void getRegressionPoints(List<Integer> advertX, List<Integer> regressX, List<Double> regressY, SimpleRegression regression) {
        int minX = Collections.min(advertX);
        int maxX = Collections.max(advertX);
        regressX.add(minX);
        regressX.add(Collections.max(advertX));
        regressY.add(regression.predict(minX));
        regressY.add(regression.predict(maxX));
    }

    private ChartDTO createIntegerChartData(String type, List<Integer> x, List<Integer> y, List<Integer> regressX, List<Double> regressY) {
        IntegerChartDTO chart = new IntegerChartDTO();
        chart.setType(type);
        chart.setAdvertX(x);
        chart.setAdvertY(y);
        chart.setRegressX(regressX);
        chart.setRegressY(regressY);
        chart.setMainChartMode(ChartMode.POINT.getValue());
        chart.setApproxChartMode(ChartMode.LINE.getValue());
        return chart;
    }

    private ChartDTO createIntegerChartData(String type, List<Integer> x, List<Integer> y, List<Integer> regressX, List<Double> regressY, SimpleRegression regression) {
        IntegerChartDTO chart = (IntegerChartDTO) createIntegerChartData(type, x, y, regressX, regressY);
        chart.setR(regression.getR());
        chart.setR2(regression.getRSquare());
        chart.setSignificance(regression.getSignificance());
        return chart;
    }

    private ChartDTO createTextChartData(String type, List<String> x, List<Integer> y, List<String> meanX, List<Double> meanY) {
        TextChartDTO chart = new TextChartDTO();
        chart.setType(type);
        chart.setAdvertX(x);
        chart.setAdvertY(y);
        chart.setRegressX(meanX);
        chart.setRegressY(meanY);
        chart.setMainChartMode(ChartMode.POINT.getValue());
        chart.setApproxChartMode(ChartMode.POINT.getValue());
        return chart;
    }
}
