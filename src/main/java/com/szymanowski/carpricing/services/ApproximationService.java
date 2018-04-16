package com.szymanowski.carpricing.services;

import com.szymanowski.carpricing.repository.Adverts;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ApproximationService {
    public List<String> approximate(List<Adverts> adverts){
        adverts.forEach(advert-> {
            advert.getPrice();
            advert.getMileage();
        });

        return new ArrayList<>();
    }
}
