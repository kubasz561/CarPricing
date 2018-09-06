package com.szymanowski.carpricing.services;

import com.szymanowski.carpricing.Utils;
import com.szymanowski.carpricing.constants.Params;
import com.szymanowski.carpricing.dto.CarData;
import com.szymanowski.carpricing.repository.Adverts;
import org.apache.commons.math3.stat.regression.SimpleRegression;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;


/**
 * Klasa jest połączeniem obiektu transferowego DTO i serwisu.
 * Jest tworzona przez ApproximationService.
 * Przechowuje informacje na temat zastosowanych filtrów, czyli parametrów wziętych pod uwagę w trakcie tworzenia regresji i średnich.
 * Umożliwia zastosowanie filtrów ponownie w trakcie przeprowadzania wyceny.
 */
public class ParametersInfo {
    /**
     * Regresja liniowa dla przebiegu
     */
    SimpleRegression mileageRegression;
    /**
     * Regresja liniowa dla roku
     */
    SimpleRegression yearRegression;
    /**
     * Mapa  obiektów typu:(Parametr pojazdu, mapa  obiektów typu:(wartośc parametru, średnia cena w zbiorze odpowiadającym tej wartości parametru))
     */
    Map<Params, Map<String, Double>> means = new HashMap<>();


    public void addMeanToMap(Params key, Map<String, Double> value){
        means.put(key,value);
    }

    public Map<String, Double> getMeanByParam(Params key){
        return means.get(key);
    }

    public SimpleRegression getMileageRegression() {
        return mileageRegression;
    }

    public void setMileageRegression(SimpleRegression mileageRegression) {
        this.mileageRegression = mileageRegression;
    }

    public SimpleRegression getYearRegression() {
        return yearRegression;
    }

    public void setYearRegression(SimpleRegression yearRegression) {
        this.yearRegression = yearRegression;
    }

    public Map<Params, Map<String, Double>> getMeans() {
        return means;
    }

    public void setMeans(Map<Params, Map<String, Double>> means) {
        this.means = means;
    }

    /**
     * Informacja czy dany parametr jest brany pod uwage w trakcie wyceny
     */
    private boolean isYear;
    private boolean isMileage;
    private boolean isEngine;
    private boolean isColor;
    private boolean isType;
    private boolean isFirstOwnerYear;
    private boolean isAccidentYear;

    /**
     * Metody zwracają wartość ceny cząstkowej dla obiektu typu CarData.
     * Każda metoda odpowiada za zwrócenie ceny cząstkowej dla innego parametru
     */
    private Function<CarData, Double> year = p -> getYearRegression().predict(p.getYear());
    private Function<CarData, Double> mileage = p -> getMileageRegression().predict(p.getMileage());
    private Function<CarData, Double> engine = p -> getMeans().get(Params.ENGINE).get(Utils.getEngineName(p));
    private Function<CarData, Double> color = p -> getMeans().get(Params.COLOR).get(p.getColor());
    private Function<CarData, Double> type = p ->  getMeans().get(Params.TYPE).get(p.getType());
    private Function<CarData, Double> firstOwnerYear = p -> getMeans().get(Params.FIRST_OWNER_YEAR).get(Utils.appendYearToParam(p, p.getIsFirstOwner()));
    private Function<CarData, Double> accidentYear = p -> getMeans().get(Params.ACCIDENT_YEAR).get(Utils.appendYearToParam(p, p.getHadAccident()));

    /**
     * Metody zwracają wartość ceny cząstkowej dla obiektu typu Adverts.
     * Każda metoda odpowiada za zwrócenie ceny cząstkowej dla innego parametru
     */
    private Function<Adverts, Double> yearA = p -> getYearRegression().predict(p.getYear());
    private Function<Adverts, Double> mileageA = p -> getMileageRegression().predict(p.getMileage());
    private Function<Adverts, Double> engineA = p -> getMeans().get(Params.ENGINE).get(Utils.getEngineName(p));
    private Function<Adverts, Double> colorA = p -> getMeans().get(Params.COLOR).get(p.getColor());
    private Function<Adverts, Double> typeA = p ->  getMeans().get(Params.TYPE).get(p.getType());
    private Function<Adverts, Double> firstOwnerYearA = p -> getMeans().get(Params.FIRST_OWNER_YEAR).get(Utils.appendYearToParam(p, p.getFirstOwner()));
    private Function<Adverts, Double> accidentYearA = p -> getMeans().get(Params.ACCIDENT_YEAR).get(Utils.appendYearToParam(p, p.getHadAccident()));

    /**
     * Filtry do stosowania na obiektach typu CarData.
     */
    private Predicate<CarData> checkYear() {
        return p -> p.getYear() != null && year.apply(p) != null;
    }
    private Predicate<CarData> checkMileage() {
        return p -> p.getMileage() != null && mileage.apply(p) != null;
    }
    private Predicate<CarData> checkEngine() {
        return p -> p.getYear() != null && p.getPower() != null && p.getEngineCapacity() != null && p.getFuel() != null && engine.apply(p) != null;
    }
    private Predicate<CarData> checkColor() {
        return p ->  p.getColor() != null && color.apply(p) != null;
    }
    private Predicate<CarData> checkType() {
        return p -> p.getType() != null && type.apply(p) != null;
    }
    private Predicate<CarData> checkFirstOwnerYear() {
        return p -> p.getYear() != null && p.getIsFirstOwner() != null && firstOwnerYear.apply(p) != null;
    }
    private Predicate<CarData> checkAccidentYear() {
        return p -> p.getYear() != null && p.getHadAccident() != null && accidentYear.apply(p) != null;
    }

    /**
     * Filtry do stosowania na obiektach typu Adverts.
     */
    private Predicate<Adverts> checkYearA() {
        return p -> p.getYear() != null && yearA.apply(p) != null;
    }
    private Predicate<Adverts> checkMileageA() {
        return p -> p.getMileage() != null && mileageA.apply(p) != null;
    }
    private Predicate<Adverts> checkEngineA() {
        return p -> p.getYear() != null && p.getPower() != null && p.getEngineCapacity() != null && p.getFuel() != null && engineA.apply(p) != null;
    }
    private Predicate<Adverts> checkColorA() {
        return p ->  p.getColor() != null && colorA.apply(p) != null;
    }
    private Predicate<Adverts> checkTypeA() {
        return p -> p.getType() != null && typeA.apply(p) != null;
    }
    private Predicate<Adverts> checkFirstOwnerYearA() {
        return p -> p.getYear() != null && p.getFirstOwner() != null && firstOwnerYearA.apply(p) != null;
    }
    private Predicate<Adverts> checkAccidentYearA() {
        return p -> p.getYear() != null && p.getHadAccident() != null && accidentYearA.apply(p) != null;
    }



    /**
     * Metoda sprawdza czy dany parametr jest brany pod uwage w trakcie wyceny
     * Parametr jest brany pod uwagę w trakcie wyceny jeżeli dla odpowiadającej mu wartości wprowadzonej przez użytkownika możliwe jest wyznaczenie ceny cząstkowej
     */
    public void calculateFilters(CarData form) {
        isYear = checkYear().test(form) ;
        isMileage = checkMileage().test(form);
        isEngine = checkEngine().test(form) ;
        isColor = checkColor().test(form) ;
        isType = checkType().test(form) ;
        isFirstOwnerYear = checkFirstOwnerYear().test(form);
        isAccidentYear = checkAccidentYear().test(form);
    }

    /**
     * Metoda zwraca predykat obejmujący wszystkie filtry do zastosowania na obiektach typu Adverts.
     * Filtr powinien być zastosowany, jeżeli odpowiadający mu parametr jest brany pod uwagę w trakcie wyceny
     */
    public Predicate<Adverts> applyAdvertFilters() {
        Predicate<Adverts> filters = checkYearA() ;

        filters = isMileage ? filters.and(checkMileageA()) : filters;
        filters = isEngine ? filters.and(checkEngineA()) : filters;
        filters = isColor ? filters.and(checkColorA()) : filters;
        filters = isType ? filters.and(checkTypeA()) : filters;
        filters = isFirstOwnerYear ? filters.and(checkFirstOwnerYearA()) : filters;
        filters = isAccidentYear ? filters.and(checkAccidentYearA()) : filters;
        return filters;
    }

    /**
     *  Metoda zwraca listę metod zwracających wartość ceny cząstkowej dla obiektu typu CarData.
     *  Lista zawiera tylko metody obliczające cenę cząstkową dla parametrów wziętych pod uwagę w trakcie wyceny.
     */
    public List<Function<CarData, Double>> getPartPriceCalculators() {
        List<Function<CarData, Double>> partPriceCalculators = new ArrayList<>();
        if(isYear) partPriceCalculators.add(year) ;
        if(isMileage) partPriceCalculators.add(mileage) ;
        if(isEngine) partPriceCalculators.add(engine) ;
        if(isColor) partPriceCalculators.add(color) ;
        if(isType) partPriceCalculators.add(type) ;
        if(isFirstOwnerYear) partPriceCalculators.add(firstOwnerYear) ;
        if(isAccidentYear) partPriceCalculators.add(accidentYear) ;
        return partPriceCalculators;

    }
    /**
     *  Metoda zwraca listę metod zwracających wartość ceny cząstkowej dla obiektu typu Adverts.
     *  Lista zawiera tylko metody obliczające cenę cząstkową dla parametrów wziętych pod uwagę w trakcie wyceny.
     */
    public List<Function<Adverts, Double>> getPartPriceCalculatorsForAdverts() {
        List<Function<Adverts, Double>> partPriceCalculators = new ArrayList<>();
        if(isYear) partPriceCalculators.add(yearA) ;
        if(isMileage) partPriceCalculators.add(mileageA) ;
        if(isEngine) partPriceCalculators.add(engineA) ;
        if(isColor) partPriceCalculators.add(colorA) ;
        if(isType) partPriceCalculators.add(typeA) ;
        if(isFirstOwnerYear) partPriceCalculators.add(firstOwnerYearA) ;
        if(isAccidentYear) partPriceCalculators.add(accidentYearA) ;
        return partPriceCalculators;

    }

    /**
     * @return nazwy parametrów wziętych pod uwagę w trakcie wyceny
     */
    public List<String> getAppliedFiltersNames() {
        List<String> appliedFilters = new ArrayList<>();
        if(isYear) appliedFilters.add(Params.YEAR.getValue());
        if(isMileage) appliedFilters.add(Params.MILEAGE.getValue());
        if(isEngine) appliedFilters.add(Params.ENGINE.getValue());
        if(isColor) appliedFilters.add(Params.COLOR.getValue());
        if(isType) appliedFilters.add(Params.TYPE.getValue());
        if(isFirstOwnerYear) appliedFilters.add(Params.FIRST_OWNER_YEAR.getValue());
        if(isAccidentYear) appliedFilters.add(Params.ACCIDENT_YEAR.getValue() );
        return appliedFilters;
    }

    /**
     * @return nazwy parametrów wziętych pod uwagę w trakcie wyceny wraz z wartościami współczynników, które im odpowiadają
     */
    public String getAppliedFiltersNamesAndValues(double[] w) {
        List<String> appliedFilters = getAppliedFiltersNames();
        if (appliedFilters.size() != w.length)
            return "Błąd - liczba wspołćzynników różna od liczby filtrów";
        StringBuilder filtersNamesAndValues = new StringBuilder();
        for (int i = 0; i < w.length; ++i) {
            filtersNamesAndValues.append(appliedFilters.get(i) + " ; " + w[i] + ", ");
        }
        return filtersNamesAndValues.toString();
    }
}
