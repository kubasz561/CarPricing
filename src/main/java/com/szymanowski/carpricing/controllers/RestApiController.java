package com.szymanowski.carpricing.controllers;

import com.szymanowski.carpricing.constants.ApproximationMethod;
import com.szymanowski.carpricing.dto.CarData;
import com.szymanowski.carpricing.dto.ChartDTO;
import com.szymanowski.carpricing.dto.LPResultDTO;
import com.szymanowski.carpricing.dto.RestResponse;
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
        Adverts clientCar = new Adverts();
       // searchService.search(form);
        List<Adverts> adverts = searchService.search(form);

        if(!CollectionUtils.isEmpty(adverts)) {
            // descriptionAnalyzerService.prepareKeywordPriceMap(adverts);
            List<ChartDTO> chartDTOS = approximationService.approximate(adverts, form);
            parametersService.calculateFilters(form);
            LPResultDTO optimizationResult = lpService.optimize(adverts);
            Double price;
            int averageDiff;
            int median;
            if (ApproximationMethod.LINEAR_PROGRAMMING.equals(form.getMethod())) {
                price = priceCalculatorService.calculatePrice(form, optimizationResult.getwParams());
                averageDiff = priceCalculatorService.calculateDiffs(adverts, optimizationResult.getwParams());
                median = priceCalculatorService.calculateMedian(adverts, optimizationResult.getwParams());
            } else {
                price = 0.0;
                averageDiff = priceCalculatorService.calculateDiffsMethodB(adverts);
                median = priceCalculatorService.calculateMedianB(adverts);
            }

            String filtersInfo = parametersService.getAppliedFiltersNamesAndValues(optimizationResult.getwParams());
            return createSearchRespone(chartDTOS, optimizationResult, price, averageDiff, median, filtersInfo);
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


    private RestResponse createSearchRespone(List<ChartDTO> chartDTOS, LPResultDTO optimizationResult, Double price, int averageDiff, int median, String filtersInfo) {
        RestResponse restResponse = new RestResponse();
        restResponse.setAverageDiff(averageDiff);
        restResponse.setMedian(median);
        restResponse.setFormPrice(price != null ? price.intValue(): 0);
        restResponse.setLpResultDTO(optimizationResult);
        restResponse.setCharts(chartDTOS);
        restResponse.setFiltersInfo(filtersInfo);
        return restResponse;
    }
    private RestResponse createSearchRespone(String message) {
        RestResponse restResponse = new RestResponse();
        restResponse.setMessage(message);
        return restResponse;
    }
}