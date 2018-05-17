package com.szymanowski.carpricing.services;

import com.szymanowski.carpricing.Utils;
import com.szymanowski.carpricing.constants.ChartMode;
import com.szymanowski.carpricing.constants.Params;
import com.szymanowski.carpricing.dto.*;
import com.szymanowski.carpricing.repository.Adverts;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.apache.commons.math3.util.Precision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.*;
import java.util.List;

@Service
public class ApproximationService {
    private final int MIN_ENGINE_COUNT = 3;

    @Autowired
    ApproximationStorage approximationStorage;

    public int calculatePrice(LPResultDTO lpResult, CarData form){
        double[] wParams = lpResult.getwParams();
        return (int) (approximationStorage.getMileageRegression().predict(form.getMileage()) * wParams[0] +
                approximationStorage.getYearRegression().predict(form.getYear()) * wParams[1] +
                approximationStorage.getMeans().get(Params.ENGINE).get(Utils.getEngineName(form)) * wParams[2]
        );
    }
    public int calculateAverageDiff(LPResultDTO lpResult, List<Adverts> adverts){ //TODO to jest duplikacja z lpSErvice
        double[] wParams = lpResult.getwParams();
        List<Double> diffs = new ArrayList<>();
        adverts.stream()
                .filter(advert -> advert.getEngineCapacity()!= null)
                .filter(advert -> advert.getFuel()!= null)
                .filter(advert -> advert.getPower()!= null)
                .filter(advert -> approximationStorage.getMeans().get(Params.ENGINE).get(Utils.getEngineName(advert)) != null)
                .forEach(advert ->
           diffs.add(advert.getPrice() - (approximationStorage.getMileageRegression().predict(advert.getMileage()) * wParams[0] +
                    approximationStorage.getYearRegression().predict(advert.getYear()) * wParams[1] +
                   approximationStorage.getMeans().get(Params.ENGINE).get(Utils.getEngineName(advert)) * wParams[2] ))
        );
        return  diffs.stream().mapToInt(i -> Math.abs(i.intValue())).sum() / diffs.size();
    }
    public List<ChartDTO> approximate(List<Adverts> adverts, CarData form){
        List<ChartDTO> charts = new ArrayList<>();

        charts.add(mileageRegression(adverts, form.getMileage()));
        charts.add(yearRegression(adverts, Integer.valueOf(form.getYear())));

        charts.add(engineMean(adverts, Utils.getEngineName(form)));

        charts.add(accidentMean(adverts,  Utils.appendYearToParam(form, form.getHadAccident())));
        charts.add(firstOwnerMean(adverts, Utils.appendYearToParam(form, form.getIsFirstOwner())));

        charts.add(colorMean(adverts, form.getColor()));
        charts.add(colorYearMean(adverts, Utils.appendYearToParam(form, form.getColor())));
        charts.add(typeMean(adverts, form.getType()));
        charts.add(typeYearMean(adverts,Utils.appendYearToParam(form, form.getType())));
/*
        charts.add(fuelMean(adverts));
        charts.add(powerMean(adverts,form.getPower().toString()+" KM"));
        charts.add(capacityMean(adverts));
        charts.add(yearmean(adverts, form.getYear()));
        charts.add(powerRegression(adverts, form.getPower()));
        charts.add(mileageNoAccidentRegression(adverts, form.getMileage()));
        charts.add(yearMileageRegression(adverts));
*/
        return charts;
    }

    private ChartDTO mileageRegression(List<Adverts> adverts, Integer formX) {
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
        Double formY = regression.predict(formX);
        getRegressionPoints(advertX,regressX,regressY, regression);
        approximationStorage.setMileageRegression(regression);
        return createIntegerChartData(Params.MILEAGE.getValue(), advertX, advertY, regressX, regressY, formX, formY, regression);
    }


    private ChartDTO yearRegression(List<Adverts> adverts, Integer formX) {
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
        Double formY = regression.predict(formX);
        approximationStorage.setYearRegression(regression);
        return createIntegerChartData(Params.YEAR.getValue(), advertX, advertY, regressX, regressY, formX, formY, regression);
    }

    private ChartDTO colorMean(List<Adverts> adverts, String formX){
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

        Double formY = getMeanPoints(regressX, regressY, colorPriceMap, means, formX);
        approximationStorage.addMeanToMap(Params.COLOR, means);
        return createTextChartData(Params.COLOR.getValue(),advertX, advertY, regressX, regressY, formX, formY);
    }
    private ChartDTO colorYearMean(List<Adverts> adverts, String formX){
        List<String> advertX = new ArrayList<>();
        List<Integer> advertY = new ArrayList<>();
        List<String> regressX = new ArrayList<>();
        List<Double> regressY = new ArrayList<>();
        MultiValueMap<String, Integer> colorPriceMap = new LinkedMultiValueMap<>();
        Map<String, Double> means = new HashMap<>();

        adverts.stream()
                .filter(advert -> advert.getColor() != null)
                .sorted(Comparator.comparing(Adverts::getYear).thenComparing(Adverts::getColor))
                .forEach(advert -> {
                    colorPriceMap.add(Utils.appendYearToParam(advert, advert.getColor()), advert.getPrice());
                    advertX.add(Utils.appendYearToParam(advert, advert.getColor()));
                    advertY.add(advert.getPrice());
                });

        Double formY = getMeanPoints(regressX, regressY, colorPriceMap, means, formX);
        approximationStorage.addMeanToMap(Params.COLOR_YEAR, means);
        return createTextChartData(Params.COLOR_YEAR.getValue(),advertX, advertY, regressX, regressY, formX, formY);
    }

    private ChartDTO accidentMean(List<Adverts> adverts, String formX){
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
                    colorPriceMap.add(Utils.appendYearToParam(advert, advert.getHadAccident()), advert.getPrice());
                    advertX.add(Utils.appendYearToParam(advert, advert.getHadAccident()));
                    advertY.add(advert.getPrice());
                });

        Double formY = getMeanPoints(regressX, regressY, colorPriceMap, means, formX);
        approximationStorage.addMeanToMap(Params.ACCIDENT_YEAR, means);
        return createTextChartData(Params.ACCIDENT_YEAR.getValue(),advertX, advertY, regressX, regressY, formX, formY); // tymczasowe odwrocenie znaczenia do czasu naprawy danych w bazie
    }

    private ChartDTO firstOwnerMean(List<Adverts> adverts, String formX){
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
                    colorPriceMap.add(Utils.appendYearToParam(advert, advert.getFirstOwner()), advert.getPrice());
                    advertX.add(Utils.appendYearToParam(advert, advert.getFirstOwner()));
                    advertY.add(advert.getPrice());
                });

        Double formY = getMeanPoints(regressX, regressY, colorPriceMap, means, formX);
        approximationStorage.addMeanToMap(Params.FIRST_OWNER_YEAR, means);
        return createTextChartData(Params.FIRST_OWNER_YEAR.getValue(),advertX, advertY, regressX, regressY, formX, formY);
    }

    private ChartDTO typeMean(List<Adverts> adverts, String formX){
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

        Double formY = getMeanPoints(regressX, regressY, colorPriceMap, means, formX);
        approximationStorage.addMeanToMap(Params.TYPE, means);

        return createTextChartData(Params.TYPE.getValue(),advertX, advertY, regressX, regressY, formX, formY);
    }

    private ChartDTO typeYearMean(List<Adverts> adverts, String formX){
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
                    colorPriceMap.add(Utils.appendYearToParam(advert, advert.getType()), advert.getPrice());
                    advertX.add(Utils.appendYearToParam(advert, advert.getType()));
                    advertY.add(advert.getPrice());
                });

        Double formY = getMeanPoints(regressX, regressY, colorPriceMap, means, formX);
        approximationStorage.addMeanToMap(Params.TYPE_YEAR, means);
        return createTextChartData(Params.TYPE_YEAR.getValue(),advertX, advertY, regressX, regressY, formX, formY);
    }




    private ChartDTO engineMean(List<Adverts> adverts, String formX){
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
                .forEach(advert ->
                    colorPriceMap.add(Utils.getEngineName(advert), advert.getPrice())
                );

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

        Double formY = getMeanPoints(regressX, regressY, filteredEnginePriceMap, means, formX);
        approximationStorage.addMeanToMap(Params.ENGINE, means);
        return createTextChartData(Params.ENGINE.getValue(),advertX, advertY, regressX, regressY, formX, formY);
    }

    private Double getMeanPoints(List<String> regressX, List<Double> regressY, MultiValueMap<String, Integer> colorPriceMap, Map<String, Double> means, String formValue) {
        colorPriceMap.keySet().forEach( key -> {
            List<Integer> values = colorPriceMap.get(key);
            Double mean = (double) values.stream().mapToInt(Integer::intValue).sum() / values.size();
            means.put(key,mean);
            regressX.add(key);
            regressY.add(mean);
        });
        return Optional.ofNullable(means.get(formValue))
                .orElse(0.0);
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
    private ChartDTO createIntegerChartData(String type, List<Integer> x, List<Integer> y, List<Integer> regressX, List<Double> regressY,
                                            Integer formX, Double formY, SimpleRegression regression) {
        IntegerChartDTO chart = (IntegerChartDTO) createIntegerChartData(type, x, y, regressX, regressY, regression);
        chart.setFormX(formX);
        chart.setFormY(formY);
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
    private ChartDTO createTextChartData(String type, List<String> x, List<Integer> y, List<String> meanX, List<Double> meanY, String formX, Double formY) {
        TextChartDTO chart = (TextChartDTO) createTextChartData(type,x,y,meanX,meanY);
        chart.setFormX(formX);
        chart.setFormY(formY);
        return chart;
    }

}
