package com.szymanowski.carpricing.services;

import com.szymanowski.carpricing.repository.Adverts;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.*;

@Service
public class DescriptionAnalyzerService {
    public MultiValueMap prepareKeywordPriceMap(List<Adverts> adverts) {
        MultiValueMap<String, Integer> keywordPriceMap = new LinkedMultiValueMap<>();
        Map<String, Double> keyCount = new HashMap<>();

        adverts.stream()
                .filter(advert -> advert.getDescription() != null)
                .forEach(advert -> {
                    String[] words = advert.getDescription().toLowerCase().split("\\W");
                    Arrays.stream(words).forEach( word ->
                        keywordPriceMap.add(word , advert.getPrice())
                    );

                });


        keywordPriceMap.keySet().forEach(key -> {
            List<Integer> values = keywordPriceMap.get(key);
             values.size();
            keyCount.put(key, (double)values.size());
        });

        return keywordPriceMap;
    }
}//problem dodawania ceny wielokrotnie jesli slowo dwa razy w opisie , zastapic pierwotnie zwykla mapa potem dodac do multivaluemap
