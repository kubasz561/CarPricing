package com.szymanowski.carpricing.services;

import com.ampl.*;

import com.szymanowski.carpricing.Utils;
import com.szymanowski.carpricing.constants.Params;
import com.szymanowski.carpricing.dto.LPResultDTO;
import com.szymanowski.carpricing.repository.Adverts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class LPService {

    @Autowired
    ApproximationStorage approximationStorage;

    public LPResultDTO optimize(List<Adverts> adverts) {
        AMPL ampl = new AMPL();

        StringBuilder totalPriceStringBuilder = new StringBuilder();
        StringBuilder partPriceStringBuilder = new StringBuilder();

        totalPriceStringBuilder.append("param C := ");
        partPriceStringBuilder.append("param c : 1 2 3:= "); //TODO automatyzacja
       // partPriceStringBuilder.append("param c : 1 2 3 4 5 6 7 := ");

    /*
        partPriceStringBuilder.append("param c : ");
        for (int k = 0; k < prices.size(); ++k) { //narazie 7
            partPriceStringBuilder.append(" " + k + "");
        }
        partPriceStringBuilder.append(" := ");*/
        List<Adverts> advertFiltered = adverts.stream()
                .filter(advert -> advert.getEngineCapacity()!= null)
                .filter(advert -> advert.getFuel()!= null)
                .filter(advert -> advert.getPower()!= null)
                .filter(advert -> approximationStorage.getMeans().get(Params.ENGINE).get(Utils.getEngineName(advert)) != null)
                .collect(Collectors.toList());


        for (int i = 0; i < advertFiltered.size(); ++i) {
            Adverts advert = advertFiltered.get(i);
            totalPriceStringBuilder.append((i+1) + " " + advert.getPrice() + " ");
            List<Double> prices = Arrays.asList(
                    approximationStorage.getMileageRegression().predict(advert.getMileage()),
                    approximationStorage.getYearRegression().predict(advert.getYear()),
                    approximationStorage.getMeans().get(Params.ENGINE).get(Utils.getEngineName(advert))

             );
/*
            List<Double> prices = Arrays.asList(
                    approximationStorage.getMileageRegression().predict(advert.getMileage()),
                    approximationStorage.getYearRegression().predict(advert.getYear()),
                    approximationStorage.getMeans().get(Params.ENGINE).get(Utils.getEngineName(advert)),
                    approximationStorage.getMeans().get(Params.COLOR_YEAR).get(Utils.appendYearToParam(advert, advert.getColor())),
                    approximationStorage.getMeans().get(Params.TYPE_YEAR).get(Utils.appendYearToParam(advert, advert.getType())),
                    approximationStorage.getMeans().get(Params.FIRST_OWNER_YEAR).get(Utils.appendYearToParam(advert, advert.getFirstOwner())),
                    approximationStorage.getMeans().get(Params.ACCIDENT_YEAR).get(Utils.appendYearToParam(advert, advert.getHadAccident()))
            );*/

            partPriceStringBuilder.append(" "+(i+1) +" ");
            for (int k = 0; k < prices.size(); ++k) {
                partPriceStringBuilder.append(" " + prices.get(k).intValue() + " ");
            }
        }
        totalPriceStringBuilder.append(";");
        partPriceStringBuilder.append(";");

        try {

            ampl.read("C:/Users/Admin/Desktop/CarPricing/ampl/model.mod");
            ampl.eval("data; param N := "+ 3+";    param M := " + advertFiltered.size() + ";  ");//TODO automatyzacja wielkosci n
            ampl.eval(totalPriceStringBuilder.toString());
            ampl.eval(partPriceStringBuilder.toString());

            /*
            ampl.eval("param C :=   1 10  2 10 3 20;");
            ampl.eval("param c :  1     2    :=   1 15  5     2 12 2       3 23 4 ;");
            */
            ampl.solve();

            Variable w = ampl.getVariable("w");


            Objective totalDiff = ampl.getObjective("f_celu");

           return createLPResult(w, totalDiff, advertFiltered.size());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            ampl.close();
        }
        return new LPResultDTO();
    }

    private LPResultDTO createLPResult(Variable v, Objective o, int filteredAdvertsSize){
        LPResultDTO lpResult = new LPResultDTO();
        lpResult.setTotalDiff(o.value());
        lpResult.setwParams(v.getValues().getColumnAsDoubles("val"));
        lpResult.setFilteredAdvertsCount(filteredAdvertsSize);
        return lpResult;
    }


}