package com.szymanowski.carpricing.controllers;

import com.szymanowski.carpricing.constants.ApproximationMethod;
import com.szymanowski.carpricing.dto.*;
import com.szymanowski.carpricing.repository.Adverts;
import com.szymanowski.carpricing.services.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.logging.Logger;

@RequestMapping("/api")
@RestController
public class RestApiController {
    @Autowired
    SearchService searchService;
    @Autowired
    ApproximationService approximationService;

    @Autowired
    PriceCalculatorService priceCalculatorService;
    @Autowired
    MakeModelService makeModelService;
    @Autowired
    LPService lpService;

    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return "Hello, World!";
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public RestResponse search(CarData form) {
        List<Adverts> adverts = form.getIsNew() ? searchService.search(form) : searchService.searchInDatabase(form);

        if(!CollectionUtils.isEmpty(adverts)) {
            ApproximationData approximationData = approximationService.approximate(adverts, form);
            ParametersInfo parametersInfo = approximationData.getParametersInfo();
            parametersInfo.calculateFilters(form);
            LPResultDTO optimizationResult = lpService.optimize(adverts, parametersInfo);
            Double price;
            int averageDiff;
            int median;
            if (ApproximationMethod.LINEAR_PROGRAMMING.equals(form.getMethod())) {
                price = priceCalculatorService.calculatePrice(form, optimizationResult.getwParams(), parametersInfo);
                averageDiff = priceCalculatorService.calculateDiffs(adverts, optimizationResult.getwParams(), parametersInfo);
                median = priceCalculatorService.calculateMedian(adverts, optimizationResult.getwParams(), parametersInfo);
            } else {
                price =  priceCalculatorService.calculateFormPriceB(form, parametersInfo);
                averageDiff = priceCalculatorService.calculateDiffsMethodB(adverts, parametersInfo);
                median = priceCalculatorService.calculateMedianB(adverts, parametersInfo);
            }

            String filtersInfo = parametersInfo.getAppliedFiltersNamesAndValues(optimizationResult.getwParams());
            return createSearchRespone(approximationData.getCharts(), optimizationResult, price, averageDiff, median, filtersInfo, form.getMethod());
        } else {
            return createSearchRespone("Nie znaleziono żadnych ogłoszeń");
        }

    }

    @RequestMapping(value = "/getMakes", method = RequestMethod.GET)
    public List<String> getMakes() {
        return makeModelService.getAllMakes();
    }

    @RequestMapping(value = "/getModels", method = RequestMethod.GET)
    public List<String> getModels(@RequestParam String make) {
        return makeModelService.getModelsForMake(make);
    }

    @RequestMapping(value = "/getVersions", method = RequestMethod.GET)
    public List<String> getVersions(@RequestParam String make, @RequestParam String model) {
        return makeModelService.getVersionForMakeModel(make,model);
    }


    private RestResponse createSearchRespone(List<ChartDTO> chartDTOS, LPResultDTO optimizationResult, Double price, int averageDiff, int median, String filtersInfo, ApproximationMethod method) {
        RestResponse restResponse = new RestResponse();
        restResponse.setAverageDiff(averageDiff);
        restResponse.setMedian(median);
        restResponse.setFormPrice(price != null ? price.intValue(): 0);
        restResponse.setLpResultDTO(optimizationResult);
        restResponse.setCharts(chartDTOS);
        if(ApproximationMethod.LINEAR_PROGRAMMING.equals(method))
            restResponse.setFiltersInfo(filtersInfo);
        return restResponse;
    }
    private RestResponse createSearchRespone(String message) {
        RestResponse restResponse = new RestResponse();
        restResponse.setMessage(message);
        return restResponse;
    }
}