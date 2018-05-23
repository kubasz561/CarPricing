package com.szymanowski.carpricing.services;

import com.szymanowski.carpricing.Utils;
import com.szymanowski.carpricing.constants.Params;
import com.szymanowski.carpricing.dto.CarData;
import com.szymanowski.carpricing.repository.Adverts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

@Service
public class ParametersService {

    @Autowired
    ApproximationStorage approximationStorage;

    private boolean isYear;
    private boolean isMileage;
    private boolean isEngine;
    private boolean isColor;
    private boolean isType;
    private boolean isFirstOwnerYear;
    private boolean isAccidentYear;

    private Function<CarData, Double> year = p -> approximationStorage.getYearRegression().predict(p.getYear());
    private Function<CarData, Double> mileage = p -> approximationStorage.getMileageRegression().predict(p.getMileage());
    private Function<CarData, Double> engine = p -> approximationStorage.getMeans().get(Params.ENGINE).get(Utils.getEngineName(p));
    private Function<CarData, Double> color = p -> approximationStorage.getMeans().get(Params.COLOR).get(p.getColor());
    private Function<CarData, Double> type = p ->  approximationStorage.getMeans().get(Params.TYPE).get(p.getType());
    private Function<CarData, Double> firstOwnerYear = p -> approximationStorage.getMeans().get(Params.FIRST_OWNER_YEAR).get(Utils.appendYearToParam(p, p.getIsFirstOwner()));
    private Function<CarData, Double> accidentYear = p -> approximationStorage.getMeans().get(Params.ACCIDENT_YEAR).get(Utils.appendYearToParam(p, p.getHadAccident()));


    private Function<Adverts, Double> yearA = p -> approximationStorage.getYearRegression().predict(p.getYear());
    private Function<Adverts, Double> mileageA = p -> approximationStorage.getMileageRegression().predict(p.getMileage());
    private Function<Adverts, Double> engineA = p -> approximationStorage.getMeans().get(Params.ENGINE).get(Utils.getEngineName(p));
    private Function<Adverts, Double> colorA = p -> approximationStorage.getMeans().get(Params.COLOR).get(p.getColor());
    private Function<Adverts, Double> typeA = p ->  approximationStorage.getMeans().get(Params.TYPE).get(p.getType());
    private Function<Adverts, Double> firstOwnerYearA = p -> approximationStorage.getMeans().get(Params.FIRST_OWNER_YEAR).get(Utils.appendYearToParam(p, p.getFirstOwner()));
    private Function<Adverts, Double> accidentYearA = p -> approximationStorage.getMeans().get(Params.ACCIDENT_YEAR).get(Utils.appendYearToParam(p, p.getHadAccident()));


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



    public void calculateFilters(CarData form) {
        isYear = checkYear().test(form) ;
        isMileage = checkMileage().test(form);
        isEngine = checkEngine().test(form) ;
        isColor = checkColor().test(form) ;
        isType = checkType().test(form) ;
        isFirstOwnerYear = checkFirstOwnerYear().test(form);
        isAccidentYear = checkAccidentYear().test(form);
    }

    public Predicate<CarData> applyFilters() { //bez sensu
        Predicate<CarData> filters = checkYear() ;

        filters = isMileage ? filters.and(checkMileage()) : filters;
        filters = isEngine ? filters.and(checkEngine()) : filters;
        filters = isColor ? filters.and(checkColor()) : filters;
        filters = isType ? filters.and(checkType()) : filters;
        filters = isFirstOwnerYear ? filters.and(checkFirstOwnerYear()) : filters;
        filters = isAccidentYear ? filters.and(checkAccidentYear()) : filters;
        return filters;
    }
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

    public List<Function<CarData, Double>> getAppliedFilters() {
        List<Function<CarData, Double>> appliedFilters = new ArrayList<>();
        if(isYear) appliedFilters.add(year) ;
        if(isMileage) appliedFilters.add(mileage) ;
        if(isEngine) appliedFilters.add(engine) ;
        if(isColor) appliedFilters.add(color) ;
        if(isType) appliedFilters.add(type) ;
        if(isFirstOwnerYear) appliedFilters.add(firstOwnerYear) ;
        if(isAccidentYear) appliedFilters.add(accidentYear) ;
        return appliedFilters;

    }
    public List<Function<Adverts, Double>> getAppliedAdvertsFilters() {
        List<Function<Adverts, Double>> appliedFilters = new ArrayList<>();
        if(isYear) appliedFilters.add(yearA) ;
        if(isMileage) appliedFilters.add(mileageA) ;
        if(isEngine) appliedFilters.add(engineA) ;
        if(isColor) appliedFilters.add(colorA) ;
        if(isType) appliedFilters.add(typeA) ;
        if(isFirstOwnerYear) appliedFilters.add(firstOwnerYearA) ;
        if(isAccidentYear) appliedFilters.add(accidentYearA) ;
        return appliedFilters;

    }

    public List<String> getAppliedFiltersNames() {
        List<String> appliedFilters = new ArrayList<>();
        if(isYear) appliedFilters.add("isYear");
        if(isMileage) appliedFilters.add("isMileage");
        if(isEngine) appliedFilters.add("isEngine");
        if(isColor) appliedFilters.add("isColor");
        if(isType) appliedFilters.add("isType");
        if(isFirstOwnerYear) appliedFilters.add("isFirstOwnerYear");
        if(isAccidentYear) appliedFilters.add("isAccidentYear" );
        return appliedFilters;
    }

    public String getAppliedFiltersNamesAndValues(double[] w) {
        List<String> appliedFilters = getAppliedFiltersNames();
        if (appliedFilters.size() != w.length)
            return "Błąd - liczba wspołćzynników różna od liczby filtrów";
        StringBuilder filtersNamesAndValues = new StringBuilder();
        for (int i = 0; i < w.length; ++i) {
            filtersNamesAndValues.append(appliedFilters.get(i) + " w: " + w[i] + ", ");
        }
        return filtersNamesAndValues.toString();
    }
    //niepotrzebne
    public void reset(){
        isYear = false;
        isMileage = false;
        isEngine = false;
        isColor = false;
        isType = false;
        isFirstOwnerYear =false;
        isAccidentYear = false;
    }


}
