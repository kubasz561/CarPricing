package com.szymanowski.carpricing.services;

import com.szymanowski.carpricing.dto.CarData;
import com.szymanowski.carpricing.repository.Adverts;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
/**
 * Odpowiada za przeprowadzenie wyceny poprzez zastosowanie średniej ważonej lub metody maksimum.
 * Odpowiada za obliczanie średniej oraz mediany odchylenia od ceny.
 */
@Service
public class PriceCalculatorService {


    /**
     * Oblicza cenę pojazdu użytkownika metodą średniej ważonej
     */
    public Double calculatePrice(CarData form, double[] w, ParametersInfo parametersInfo) {
        if (w.length != parametersInfo.getPartPriceCalculators().size())
            return null;

        double sum = 0;
        for (int i = 0; i < w.length; ++i) {
            sum += parametersInfo.getPartPriceCalculators().get(i).apply(form) * w[i];
        }
        return sum;
    }

    /**
     * Oblicza cenę dla pojedynczego ogłoszenia metodą średniej ważonej
     */
    public Double calculateAdvertPrice(Adverts form, double[] w, ParametersInfo parametersInfo) {
        if (w.length != parametersInfo.getPartPriceCalculators().size())
            return null;

        double sum = 0;
        for (int i = 0; i < w.length; ++i) {
            sum += parametersInfo.getPartPriceCalculatorsForAdverts().get(i).apply(form) * w[i];
        }
        return sum;
    }

    /**
     * Oblicza średnie odchylenie od ceny dla metody średniej ważonej
     */
    public int calculateDiffs(List<Adverts> adverts, double[] w, ParametersInfo parametersInfo) {

        List<Double> diffs = new ArrayList<>();

        adverts.stream()
                .filter(p -> parametersInfo.applyAdvertFilters().test(p))
                .forEach(advert ->
                        diffs.add(advert.getPrice() - calculateAdvertPrice(advert, w, parametersInfo))
                );

        return (int) diffs.stream().mapToInt(i -> Math.abs(i.intValue())).average().getAsDouble();
    }

    /**
     * Oblicza jaki procent średniej ceny modelu stanowi mediana odchylenia od ceny.
     */
    public int calculateDiffMedianPercent(List<Adverts> adverts, int medianDiff) {


        return (int) (medianDiff*100/adverts.stream()
                .map(Adverts::getPrice).mapToInt(i -> Math.abs(i.intValue())).average().getAsDouble());

    }

    /**
     * Oblicza medianę odchylenia od ceny dla metody średniej ważonej
     */
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

    /**
     * Oblicza medianę odchylenia od ceny dla metody maksimum
     */
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

    /**
     * Oblicza cenę dla pojedynczego ogłoszenia metodą maksimum
     */
    public int calculateAdvertPriceB(Adverts form, ParametersInfo parametersInfo) {
        List<Double> prices = new ArrayList<>();
        parametersInfo.getPartPriceCalculatorsForAdverts().forEach(filter ->
                prices.add(filter.apply(form))
        );
        return prices.stream().mapToInt(i -> i.intValue()).max().getAsInt();
    }

    /**
     * Oblicza cenę pojazdu użytkownika metodą maksimum
     */
    public double calculateFormPriceB(CarData form, ParametersInfo parametersInfo) {
        List<Double> prices = new ArrayList<>();
        parametersInfo.getPartPriceCalculators().forEach(filter ->
                prices.add(filter.apply(form))
        );
        return (double) prices.stream().mapToInt(i -> i.intValue()).max().getAsInt();
    }

    /**
     * Oblicza średnie odchylenie od ceny dla metody maksimum
     */
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
