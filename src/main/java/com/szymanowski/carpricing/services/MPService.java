package com.szymanowski.carpricing.services;

import com.ampl.*;

import com.szymanowski.carpricing.dto.MPResultDTO;
import com.szymanowski.carpricing.repository.Adverts;
import org.apache.commons.math3.util.Precision;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class MPService {


    public MPResultDTO optimize(List<Adverts> adverts, ParametersInfo parametersInfo) {
        AMPL ampl = new AMPL();

        AmplDataDTO amplDataDTO = new AmplDataDTO(adverts, parametersInfo).invoke();
        List<Adverts> advertFiltered = amplDataDTO.getAdvertFiltered();

        try {
            ampl.read("ampl/models/model.mod");
            ampl.eval("data; param N := " + parametersInfo.getAppliedFilters().size() + ";    param M := " + advertFiltered.size() + ";  ");
            ampl.eval(amplDataDTO.getTotalPriceStringBuilder().toString());
            ampl.eval(amplDataDTO.getPartPriceStringBuilder().toString());

            ampl.solve();

            Variable w = ampl.getVariable("w");

            Objective totalDiff = ampl.getObjective("f_celu");

            return createMPResult(w, totalDiff, advertFiltered.size());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            ampl.close();
        }
        return new MPResultDTO();
    }

    private MPResultDTO createMPResult(Variable v, Objective o, int filteredAdvertsSize){
        MPResultDTO mpResult = new MPResultDTO();
        mpResult.setTotalDiff((int)o.value());
        mpResult.setwParams(roundWParams(v.getValues().getColumnAsDoubles("val")));
        mpResult.setFilteredAdvertsCount(filteredAdvertsSize);
        return mpResult;
    }
    private double[] roundWParams(double[] wParams){
       return Arrays.stream(wParams).map(w -> Precision.round(w, 2)).toArray();
    }


    private class AmplDataDTO {
        private List<Adverts> adverts;
        private ParametersInfo parametersInfo;
        private StringBuilder totalPriceStringBuilder;
        private StringBuilder partPriceStringBuilder;
        private List<Adverts> advertFiltered;

        public AmplDataDTO(List<Adverts> adverts, ParametersInfo parametersInfo) {
            this.adverts = adverts;
            this.parametersInfo = parametersInfo;
        }

        public StringBuilder getTotalPriceStringBuilder() {
            return totalPriceStringBuilder;
        }

        public StringBuilder getPartPriceStringBuilder() {
            return partPriceStringBuilder;
        }

        public List<Adverts> getAdvertFiltered() {
            return advertFiltered;
        }

        public AmplDataDTO invoke() {
            totalPriceStringBuilder = new StringBuilder();
            partPriceStringBuilder = new StringBuilder();

            totalPriceStringBuilder.append("param C := ");
            partPriceStringBuilder.append("param c : ");

            for (int k = 0; k < parametersInfo.getAppliedFilters().size(); ++k) {
                partPriceStringBuilder.append(" " + (k + 1) + "");
            }

            partPriceStringBuilder.append(" := ");

            advertFiltered = adverts.stream()
                    .filter(parametersInfo.applyAdvertFilters())
                    .collect(Collectors.toList());

            for (int i = 0; i < advertFiltered.size(); ++i) {
                Adverts advert = advertFiltered.get(i);
                totalPriceStringBuilder.append((i + 1) + " " + advert.getPrice() + " ");
                partPriceStringBuilder.append(" " + (i + 1) + " ");

                List<Double> prices = new ArrayList<>();

                parametersInfo.getAppliedAdvertsFilters().forEach(filter -> prices.add(filter.apply(advert)));

                prices.forEach(price ->
                    partPriceStringBuilder.append(" " + price.intValue() + " ")
                );
            }
            totalPriceStringBuilder.append(";");
            partPriceStringBuilder.append(";");
            return this;
        }
    }
}