package com.szymanowski.carpricing;

import com.szymanowski.carpricing.dto.CarData;
import com.szymanowski.carpricing.repository.Adverts;

import java.util.Optional;

public class Utils {

    public static String getEngineName(Adverts advert) {
        double capacityRounded = Math.round((double) advert.getEngineCapacity() / 100);
        return capacityRounded + "dm3, " + advert.getPower() + "KM, " + advert.getFuel();
    }

    public static String getEngineName(CarData form){
        double capacityRounded = Math.round((double)form.getEngineCapacity() /100);
        return capacityRounded + "dm3, " + form.getPower() + "KM, "+ form.getFuel();
    }
    public static String appendYearToParam(CarData form, Object o){
        return o != null ? form.getYear() + "-" + o.toString() : form.getYear() + " -empty";
    }
    public static String appendYearToParam(Adverts form, Object o){
        return o != null ? form.getYear() + "-" + o.toString() : form.getYear() + " -empty";
    }

}
