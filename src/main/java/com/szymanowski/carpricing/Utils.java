package com.szymanowski.carpricing;

import com.szymanowski.carpricing.dto.CarData;
import com.szymanowski.carpricing.repository.Adverts;

import java.util.Calendar;
import java.util.Date;

/**
 * Użyteczne metody
 */
public class Utils {

    /**
     * Tworzy nazwę silnika dla obiektu typu Advert
     */
    public static String getEngineName(Adverts advert) {
        double capacityRounded = Math.round((double) advert.getEngineCapacity() / 100);
        return capacityRounded + "dm3, " + advert.getPower() + "KM, " + advert.getFuel();
    }

    /**
     * Tworzy nazwę silnika dla obiektu typu CarData
     */
    public static String getEngineName(CarData form){
        double capacityRounded = Math.round((double)form.getEngineCapacity() /100);
        return capacityRounded + "dm3, " + form.getPower() + "KM, "+ form.getFuel();
    }

    /**
     * łączy parametr z rokiem dla obiektu typu CarData
     */
    public static String appendYearToParam(CarData form, Object o){
        return o != null ? form.getYear() + "-" + o.toString() : form.getYear() + " -empty";
    }

    /**
     * łączy parametr z rokiem dla obiektu typu Adverts
     */
    public static String appendYearToParam(Adverts form, Object o){
        return o != null ? form.getYear() + "-" + o.toString() : form.getYear() + " -empty";
    }

    /**
     * Zwraca datę sprzed roku
     */
    public static Date getYearAgoDate() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -1);
        return cal.getTime();
    }
}
