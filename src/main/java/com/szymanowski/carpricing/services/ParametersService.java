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

    public Function<CarData, Double> year = p -> approximationStorage.getYearRegression().predict(p.getYear());
    public Function<CarData, Double> mileage = p -> approximationStorage.getMileageRegression().predict(p.getMileage());
    public Function<CarData, Double> engine = p -> approximationStorage.getMeans().get(Params.ENGINE).get(Utils.getEngineName(p));
    public Function<CarData, Double> color = p -> approximationStorage.getMeans().get(Params.COLOR).get(p.getColor());
    public Function<CarData, Double> type = p ->  approximationStorage.getMeans().get(Params.TYPE).get(p.getType());
    public Function<CarData, Double> firstOwnerYear = p -> approximationStorage.getMeans().get(Params.FIRST_OWNER_YEAR).get(Utils.appendYearToParam(p, p.getIsFirstOwner()));
    public Function<CarData, Double> accidentYear = p -> approximationStorage.getMeans().get(Params.ACCIDENT_YEAR).get(Utils.appendYearToParam(p, p.getHadAccident()));


    public Predicate<CarData> checkYear() {
        return p -> p.getYear() != null && year.apply(p) != null;
    }
    public Predicate<CarData> checkMileage() {
        return p -> p.getMileage() != null && mileage.apply(p) != null;
    }
    public Predicate<CarData> checkEngine() {
        return p -> engine != null;
    }
    public Predicate<CarData> checkColor() {
        return p ->  color != null;
    }
    public Predicate<CarData> checkType() {
        return p -> type != null;
    }
    public Predicate<CarData> checkFirstOwnerYear() {
        return p -> firstOwnerYear != null;
    }
    public Predicate<CarData> checkAccidentYear() {
        return p -> accidentYear != null;
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

    public Predicate<CarData> applyFilters(CarData form) {
        Predicate<CarData> filters = checkYear() ;

        filters = checkMileage().test(form) ? filters.and(checkMileage()) : filters;
        filters = checkEngine().test(form) ? filters.and(checkEngine()) : filters;
        filters = checkColor().test(form) ? filters.and(checkColor()) : filters;
        filters = checkType().test(form) ? filters.and(checkType()) : filters;
        filters = checkFirstOwnerYear().test(form) ? filters.and(checkFirstOwnerYear()) : filters;
        filters = checkAccidentYear().test(form) ? filters.and(checkAccidentYear()) : filters;
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
    public Double calculatePrice(CarData form, double[] w ) {
        if (w.length != getAppliedFilters().size())
            return null;

        double sum = 0;
        for (int i = 0; i < w.length ; ++i){
            sum += getAppliedFilters().get(i).apply(form) * w[i];
        }


        return sum;
    }

    public int calculateDiffs(CarData form, List<Adverts> adverts, double[] w) {

        List<Double> diffs = new ArrayList<>();

        adverts.stream().filter(p -> applyFilters(form).test(p)).forEach(advert -> {
            CarData formTemp = new CarData();
            formTemp.setYear(advert.getYear());


            diffs.add(advert.getPrice() - calculatePrice(advert, w));
        });


        return diffs.stream().mapToInt(i -> Math.abs(i.intValue())).sum() / diffs.size();
    }

    public List<String> getAppliedFiltersNames() {
        List<String> appliedFilters = new ArrayList<>();
        appliedFilters.add(isYear ? "isYear, " : "" );
        appliedFilters.add(isMileage ? "isMileage, " : "" );
        appliedFilters.add(isEngine ? "isEngine, " : "" );
        appliedFilters.add(isColor ? "isColor, " : "" );
        appliedFilters.add(isType ? "isType, " : "" );
        appliedFilters.add(isFirstOwnerYear ? "isFirstOwnerYear, " : "" );
        appliedFilters.add(isAccidentYear ? "isAccidentYear, " : "" );
        return appliedFilters;
    }


}
