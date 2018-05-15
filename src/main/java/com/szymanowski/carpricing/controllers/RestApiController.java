package com.szymanowski.carpricing.controllers;

import com.szymanowski.carpricing.dto.CarData;
import com.szymanowski.carpricing.dto.ChartDTO;
import com.szymanowski.carpricing.repository.Adverts;
import com.szymanowski.carpricing.services.ApproximationService;
import com.szymanowski.carpricing.services.DescriptionAnalyzerService;
import com.szymanowski.carpricing.services.SearchService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.logging.Logger;

@RequestMapping("/api")
@RestController
public class RestApiController {
    private static final Logger LOG = Logger.getLogger("RestApiController");
    @Autowired
    SearchService searchService;
    @Autowired
    ApproximationService approximationService;
    @Autowired
    DescriptionAnalyzerService descriptionAnalyzerService;

    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return "Hello, World!";
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public List<ChartDTO> search(CarData form) {
        Adverts clientCar = new Adverts();
       // searchService.search(form);
        List<Adverts> adverts = searchService.searchInDatabase(form);

       // return searchService.search(form);
        descriptionAnalyzerService.prepareKeywordPriceMap(adverts);
        return approximationService.approximate(adverts, form);

    }
}