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
    MPService mPService;

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public RestResponse search(CarData form) {
        List<Adverts> adverts = form.getSearchNewAdverts() ? searchService.search(form) : searchService.searchInDatabase(form);

        if(!CollectionUtils.isEmpty(adverts)) {
            ApproximationData approximationData = approximationService.approximate(adverts, form);
            ParametersInfo parametersInfo = approximationData.getParametersInfo();
            parametersInfo.calculateFilters(form);
            if (ApproximationMethod.WEIGHTED_MEAN.equals(form.getMethod())) {
                MPResultDTO optimizationResult = mPService.optimize(adverts, parametersInfo);
                Double price = priceCalculatorService.calculatePrice(form, optimizationResult.getwParams(), parametersInfo);
                int averageDiff = priceCalculatorService.calculateDiffs(adverts, optimizationResult.getwParams(), parametersInfo);
                int median = priceCalculatorService.calculateMedian(adverts, optimizationResult.getwParams(), parametersInfo);
                int diffPercent = priceCalculatorService.calculateDiffMedianPercent(adverts, median);
                String filtersInfo = parametersInfo.getAppliedFiltersNamesAndValues(optimizationResult.getwParams());
                return createSearchResponse(approximationData.getCharts(), optimizationResult.getFilteredAdvertsCount(), price, averageDiff, median, diffPercent, filtersInfo, form.getMethod());
            } else {
                Double price =  priceCalculatorService.calculateFormPriceB(form, parametersInfo);
                int averageDiff = priceCalculatorService.calculateDiffsMethodB(adverts, parametersInfo);
                int median = priceCalculatorService.calculateMedianB(adverts, parametersInfo);
                int diffPercent = priceCalculatorService.calculateDiffMedianPercent(adverts, median);
                return createSearchResponse(approximationData.getCharts(), adverts.size(), price, averageDiff, median, diffPercent, null, form.getMethod());
            }
        } else {
            return createSearchResponse("Nie znaleziono żadnych ogłoszeń");
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


    private RestResponse createSearchResponse(List<ChartDTO> chartDTOS, int advertsCount, Double price, int averageDiff, int median, int diffPercent, String filtersInfo, ApproximationMethod method) {
        RestResponse restResponse = new RestResponse();
        restResponse.setAverageDiff(averageDiff);
        restResponse.setDiffPercent(diffPercent);
        restResponse.setMedian(median);
        restResponse.setFormPrice(price != null ? price.intValue(): 0);
        restResponse.setAdvertsCount(advertsCount);
        restResponse.setCharts(chartDTOS);
        restResponse.setFiltersInfo(filtersInfo);
        return restResponse;
    }
    private RestResponse createSearchResponse(String message) {
        RestResponse restResponse = new RestResponse();
        restResponse.setMessage(message);
        return restResponse;
    }
}