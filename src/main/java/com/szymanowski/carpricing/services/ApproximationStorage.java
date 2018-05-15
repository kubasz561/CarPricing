package com.szymanowski.carpricing.services;

import com.szymanowski.carpricing.constants.Params;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Scope(value = "request")
public class ApproximationStorage {

    SimpleRegression mileageRegression;
    SimpleRegression yearRegression;
    Map<Params, Map<String, Double>> means = new HashMap<>();


    public void addMeanToMap(Params key, Map<String, Double> value){
        means.put(key,value);
    }
    public Map<String, Double> getMeanByParam(Params key){
        return means.get(key);
    }

    public void reset(){
        mileageRegression = null;
        yearRegression = null;
        means = new HashMap<>();
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


}
