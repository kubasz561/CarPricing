package com.szymanowski.carpricing.services;

import com.szymanowski.carpricing.constants.ChartMode;
import com.szymanowski.carpricing.dto.ChartDTO;
import com.szymanowski.carpricing.dto.IntegerChartDTO;
import com.szymanowski.carpricing.dto.TextChartDTO;
import com.szymanowski.carpricing.repository.Adverts;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.apache.commons.math3.util.Precision;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.*;

@Service
public class ApproximationService {
    private final int MIN_ENGINE_COUNT = 3;

    public List<ChartDTO> approximate(List<Adverts> adverts){
        List<ChartDTO> charts = new ArrayList<>();

        charts.add(mileageRegression(adverts));
        charts.add(mileageNoAccidentRegression(adverts));
        charts.add(yearRegression(adverts));
        charts.add(yearmean(adverts));
        charts.add(engineMean(adverts));
        charts.add(colorMean(adverts));
        charts.add(firstOwnerMean(adverts));
        charts.add(accidentMean(adverts));
        charts.add(typeMean(adverts));
        charts.add(typeYearMean(adverts));

        charts.add(fuelMean(adverts));
        charts.add(powerMean(adverts));
        charts.add(capacityMean(adverts));
        //charts.add(yearMileageRegression(adverts));
        //charts.add(powerRegression(adverts));

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
                .filter(advert -> Boolean.TRUE.equals(advert.getHadAccident())) // tymczasowe odwrocenie znaczenia do czasu naprawy danych w bazie
                .forEach(advert -> {
                    regression.addData(advert.getMileage(), advert.getPrice());
                    advertX.add(advert.getMileage());
                    advertY.add(advert.getPrice());
                });

        getRegressionPoints(advertX,regressX,regressY, regression);
        return createIntegerChartData("Mileage no Accident", advertX, advertY, regressX, regressY, regression);
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

    private ChartDTO yearmean(List<Adverts> adverts){
        List<String> advertX = new ArrayList<>();
        List<Integer> advertY = new ArrayList<>();
        List<String> regressX = new ArrayList<>();
        List<Double> regressY = new ArrayList<>();
        MultiValueMap<String, Integer> colorPriceMap = new LinkedMultiValueMap<>();
        Map<String, Double> means = new HashMap<>();

        adverts.stream()
                .filter(advert -> advert.getYear() != null)
                .sorted(Comparator.comparing(Adverts::getYear))
                .forEach(advert -> {
                    colorPriceMap.add(advert.getYear().toString(), advert.getPrice());
                    advertX.add(advert.getYear().toString());
                    advertY.add(advert.getPrice());
                });

        getMeanPoints(regressX, regressY, colorPriceMap, means);
        return createTextChartData("Year Mean",advertX, advertY, regressX, regressY);
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

    private ChartDTO accidentMean(List<Adverts> adverts){
        List<String> advertX = new ArrayList<>();
        List<Integer> advertY = new ArrayList<>();
        List<String> regressX = new ArrayList<>();
        List<Double> regressY = new ArrayList<>();
        MultiValueMap<String, Integer> colorPriceMap = new LinkedMultiValueMap<>();
        Map<String, Double> means = new HashMap<>();

        adverts.stream()
                .filter(advert -> advert.getHadAccident() != null)
                .filter(advert -> advert.getYear() != null)
                .sorted(Comparator.comparing(Adverts::getYear).thenComparing(Adverts::getHadAccident))
                .forEach(advert -> {
                    colorPriceMap.add(advert.getYear()+advert.getHadAccident().toString(), advert.getPrice());
                    advertX.add(advert.getYear()+advert.getHadAccident().toString());
                    advertY.add(advert.getPrice());
                });

        getMeanPoints(regressX, regressY, colorPriceMap, means);
        return createTextChartData("Bezwypadkowy",advertX, advertY, regressX, regressY); // tymczasowe odwrocenie znaczenia do czasu naprawy danych w bazie
    }

    private ChartDTO firstOwnerMean(List<Adverts> adverts){
        List<String> advertX = new ArrayList<>();
        List<Integer> advertY = new ArrayList<>();
        List<String> regressX = new ArrayList<>();
        List<Double> regressY = new ArrayList<>();
        MultiValueMap<String, Integer> colorPriceMap = new LinkedMultiValueMap<>();
        Map<String, Double> means = new HashMap<>();

        adverts.stream()
                .filter(advert -> advert.getFirstOwner() != null)
                .filter(advert -> advert.getYear() != null)
                .sorted(Comparator.comparing(Adverts::getYear).thenComparing(Adverts::getFirstOwner))
                .forEach(advert -> {
                    colorPriceMap.add(advert.getYear()+advert.getFirstOwner().toString(), advert.getPrice());
                    advertX.add(advert.getYear()+advert.getFirstOwner().toString());
                    advertY.add(advert.getPrice());
                });

        getMeanPoints(regressX, regressY, colorPriceMap, means);
        return createTextChartData("First Owner",advertX, advertY, regressX, regressY);
    }

    private ChartDTO typeMean(List<Adverts> adverts){
        List<String> advertX = new ArrayList<>();
        List<Integer> advertY = new ArrayList<>();
        List<String> regressX = new ArrayList<>();
        List<Double> regressY = new ArrayList<>();
        MultiValueMap<String, Integer> colorPriceMap = new LinkedMultiValueMap<>();
        Map<String, Double> means = new HashMap<>();

        adverts.stream()
                .filter(advert -> advert.getType() != null)
                .sorted(Comparator.comparing(Adverts::getType))
                .forEach(advert -> {
                    colorPriceMap.add(advert.getType(), advert.getPrice());
                    advertX.add(advert.getType());
                    advertY.add(advert.getPrice());
                });

        getMeanPoints(regressX, regressY, colorPriceMap, means);
        return createTextChartData("Type",advertX, advertY, regressX, regressY);
    }

    private ChartDTO typeYearMean(List<Adverts> adverts){
        List<String> advertX = new ArrayList<>();
        List<Integer> advertY = new ArrayList<>();
        List<String> regressX = new ArrayList<>();
        List<Double> regressY = new ArrayList<>();
        MultiValueMap<String, Integer> colorPriceMap = new LinkedMultiValueMap<>();
        Map<String, Double> means = new HashMap<>();

        adverts.stream()
                .filter(advert -> advert.getType() != null)
                .filter(advert -> advert.getYear() != null)
                .sorted(Comparator.comparing(Adverts::getYear).thenComparing(Adverts::getType))
                .forEach(advert -> {
                    colorPriceMap.add(advert.getYear()+advert.getType(), advert.getPrice());
                    advertX.add(advert.getYear()+advert.getType());
                    advertY.add(advert.getPrice());
                });

        getMeanPoints(regressX, regressY, colorPriceMap, means);
        return createTextChartData("Type Year",advertX, advertY, regressX, regressY);
    }




    private ChartDTO engineMean(List<Adverts> adverts){
        List<String> advertX = new ArrayList<>();
        List<Integer> advertY = new ArrayList<>();
        List<String> regressX = new ArrayList<>();
        List<Double> regressY = new ArrayList<>();
        MultiValueMap<String, Integer> colorPriceMap = new LinkedMultiValueMap<>();
        MultiValueMap<String, Integer> filteredEnginePriceMap = new LinkedMultiValueMap<>();
        Map<String, Double> means = new HashMap<>();

        adverts.stream()
                .filter(advert -> advert.getFuel() != null)
                .filter(advert -> advert.getPower() != null)
                .filter(advert -> advert.getEngineCapacity() != null)
                .sorted(Comparator.comparing(Adverts::getPower).thenComparing(Adverts::getEngineCapacity).thenComparing(Adverts::getFuel))
                .forEach(advert -> {
                    double capacityRounded = Math.round((double)advert.getEngineCapacity()/100);
                    String engine = capacityRounded + "dm3, " + advert.getPower() + "KM, "+ advert.getFuel();
                    colorPriceMap.add(engine, advert.getPrice());

                });

        colorPriceMap.keySet().forEach( key -> {
            List<Integer> values = colorPriceMap.get(key);
            if(values.size() >= MIN_ENGINE_COUNT) {
                filteredEnginePriceMap.addAll(key, values);
            }
        });
        filteredEnginePriceMap.forEach((key, values) -> {
            values.forEach(value -> {
                advertX.add(key);
                advertY.add(value);
            });
        });

        getMeanPoints(regressX, regressY, filteredEnginePriceMap, means);
        return createTextChartData("Engine",advertX, advertY, regressX, regressY);
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
        chart.setR(Precision.round(regression.getR(), 2));
        chart.setR2(Precision.round(regression.getRSquare(),2));
        chart.setSignificance(Precision.round(regression.getSignificance(),2));
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

}
