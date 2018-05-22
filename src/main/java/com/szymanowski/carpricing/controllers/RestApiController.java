package com.szymanowski.carpricing.controllers;

import com.szymanowski.carpricing.dto.CarData;
import com.szymanowski.carpricing.dto.ChartDTO;
import com.szymanowski.carpricing.dto.LPResultDTO;
import com.szymanowski.carpricing.dto.RestResponse;
import com.szymanowski.carpricing.repository.Adverts;
import com.szymanowski.carpricing.services.*;

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
    @Autowired
    ParametersService parametersService;

    @Autowired
    LPService lpService;

    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return "Hello, World!";
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public RestResponse search(CarData form) {
        Adverts clientCar = new Adverts();
       // searchService.search(form);
        List<Adverts> adverts = searchService.searchInDatabase(form);

       // return searchService.search(form);
       // descriptionAnalyzerService.prepareKeywordPriceMap(adverts);
        List<ChartDTO> chartDTOS = approximationService.approximate(adverts, form);
        parametersService.calculateFilters(form);
        LPResultDTO optimizationResult = lpService.optimize(adverts);
        Double price =  parametersService.calculatePrice(form, optimizationResult.getwParams());
        int averageDiff = parametersService.calculateDiffs(adverts, optimizationResult.getwParams());
        String filtersInfo = parametersService.getAppliedFiltersNames().toString() + parametersService.getAppliedFiltersNames().size();
        return createResponse(chartDTOS, optimizationResult, price != null ? price.intValue(): 0, averageDiff);

    }

    private RestResponse createResponse(List<ChartDTO> chartDTOS, LPResultDTO optimizationResult, int price, int averageDiff) {
        RestResponse restResponse = new RestResponse();
        restResponse.setAverageDiff(averageDiff);
        restResponse.setFormPrice(price);
        restResponse.setLpResultDTO(optimizationResult);
        restResponse.setCharts(chartDTOS);
        return restResponse;
    }
}