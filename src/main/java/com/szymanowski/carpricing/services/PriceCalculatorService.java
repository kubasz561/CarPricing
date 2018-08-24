package com.szymanowski.carpricing.services;

import com.szymanowski.carpricing.dto.CarData;
import com.szymanowski.carpricing.repository.Adverts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PriceCalculatorService {


    public Double calculatePrice(CarData form, double[] w, ParametersInfo parametersInfo) {
        if (w.length != parametersInfo.getAppliedFilters().size())
            return null;

        double sum = 0;
        for (int i = 0; i < w.length; ++i) {
            sum += parametersInfo.getAppliedFilters().get(i).apply(form) * w[i];
        }
        return sum;
    }

    public Double calculateAdvertPrice(Adverts form, double[] w, ParametersInfo parametersInfo) {
        if (w.length != parametersInfo.getAppliedFilters().size())
            return null;

        double sum = 0;
        for (int i = 0; i < w.length; ++i) {
            sum += parametersInfo.getAppliedAdvertsFilters().get(i).apply(form) * w[i];
        }
        return sum;
    }

    public int calculateDiffs(List<Adverts> adverts, double[] w, ParametersInfo parametersInfo) {

        List<Double> diffs = new ArrayList<>();

        adverts.stream()
                .filter(p -> parametersInfo.applyAdvertFilters().test(p))
                .forEach(advert ->
                        diffs.add(advert.getPrice() - calculateAdvertPrice(advert, w, parametersInfo))
                );

        return (int) diffs.stream().mapToInt(i -> Math.abs(i.intValue())).average().getAsDouble();
    }

    public int calculateMedian(List<Adverts> adverts, double[] w, ParametersInfo parametersInfo) {
        List<Double> diffs = new ArrayList<>();

        adverts.stream()
                .filter(p -> parametersInfo.applyAdvertFilters().test(p))
                .forEach(advert ->
                        diffs.add(advert.getPrice() - calculateAdvertPrice(advert, w, parametersInfo))
                );

        int[] sortedDiffs = diffs.stream().mapToInt(i -> Math.abs(i.intValue())).sorted().toArray();
        int medium = sortedDiffs.length / 2;
        if (sortedDiffs.length % 2 == 0 && sortedDiffs.length > 2)
            return (sortedDiffs[medium - 1] + sortedDiffs[medium]) / 2;
        else
            return sortedDiffs[medium];
    }

    public int calculateMedianB(List<Adverts> adverts, ParametersInfo parametersInfo) {
        List<Integer> diffs = new ArrayList<>();

        adverts.stream()
                .filter(p -> parametersInfo.applyAdvertFilters().test(p))
                .forEach(advert ->
                        diffs.add(advert.getPrice() - calculateAdvertPriceB(advert, parametersInfo))
                );
        int[] sortedDiffs = diffs.stream().mapToInt(i -> Math.abs(i.intValue())).sorted().toArray();
        int medium = sortedDiffs.length / 2;
        if (sortedDiffs.length % 2 == 0 && sortedDiffs.length > 2)
            return (sortedDiffs[medium - 1] + sortedDiffs[medium]) / 2;
        else
            return sortedDiffs[medium];
    }

    public int calculateAdvertPriceB(Adverts form, ParametersInfo parametersInfo) {
        List<Double> prices = new ArrayList<>();
        parametersInfo.getAppliedAdvertsFilters().forEach(filter ->
                prices.add(filter.apply(form))
        );
        return prices.stream().mapToInt(i -> i.intValue()).max().getAsInt();
    }
    public double calculateFormPriceB(CarData form, ParametersInfo parametersInfo) {
        List<Double> prices = new ArrayList<>();
        parametersInfo.getAppliedFilters().forEach(filter ->
                prices.add(filter.apply(form))
        );
        return (double) prices.stream().mapToInt(i -> i.intValue()).max().getAsInt();
    }

    public int calculateDiffsMethodB(List<Adverts> adverts, ParametersInfo parametersInfo) {

        List<Integer> diffs = new ArrayList<>();

        adverts.stream()
                .filter(p -> parametersInfo.applyAdvertFilters().test(p))
                .forEach(advert ->
                        diffs.add(advert.getPrice() - calculateAdvertPriceB(advert, parametersInfo))
                );

        return (int) diffs.stream().mapToInt(i -> Math.abs(i.intValue())).average().getAsDouble();
    }
}
