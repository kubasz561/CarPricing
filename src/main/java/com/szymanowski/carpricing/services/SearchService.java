package com.szymanowski.carpricing.services;

import com.szymanowski.carpricing.Utils;
import com.szymanowski.carpricing.dto.CarData;
import com.szymanowski.carpricing.parser.AdvertParser;
import com.szymanowski.carpricing.repository.Adverts;
import com.szymanowski.carpricing.repository.AdvertsRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Klasa dpowiada za wyszukiwanie ogłoszeń w serwisie Otomoto.pl oraz w bazie danych.
 * Jeżeli ogłoszenia nie ma w bazie danych to jest w niej zapisywane.
 * Do parsowania kodu HTML ogłoszeń używa klasy AdvertParser.
 */
@Service
public class SearchService {
    private static final Logger LOG = LoggerFactory.getLogger(SearchService.class);

    private static final String BASE = "https://www.otomoto.pl/osobowe/";
    private static final String END = "/?page=";

    @Autowired
    AdvertParser advertParser;

    @Autowired
    private AdvertsRepository advertsRepository;

    /**
     * Wyszukiwanie ogłoszeń w bazie danych zapisanych nie dawniej niż rok temu
     * @param form - formularz zawierające dane dotyczące marki, modelu i wersji
     * @return lista ogłoszeń
     */
    public List<Adverts> searchInDatabase(CarData form) {
        return StringUtils.isEmpty(form.getVersion()) ? advertsRepository.findByMakeAndModelAndSaveDateAfter(form.getMake(), form.getModel(), Utils.getYearAgoDate())
                : advertsRepository.findByMakeAndModelAndVersionAndSaveDateAfter(form.getMake(),form.getModel(), form.getVersion(), Utils.getYearAgoDate());
    }

    /**
     * Wyszukiwanie i pobieranie danych ogłoszeń z serwisu Otomoto.pl
     * Jeżeli ogłoszenia nie ma w bazie danych, to jest w niej zapisywane.
     * @param form - formularz zawierające dane dotyczące marki, modelu i wersji
     * @return lista ogłoszeń
     */
    public List<Adverts> search(CarData form) {
        List<Adverts> adverts = new ArrayList<>();
        List<Adverts> databaseAdverts = searchInDatabase(form);
        String url = getCarUrl(form);
        try {
            Document doc = Jsoup.connect(url + 1).get();
            int numberOfPages = getNumberOfPages(doc);
            for (int i = 1; i <= numberOfPages; ++i) {
                try {
                    Document pageList = Jsoup.connect(url + i).get();

                    List<String> hrefs = getAdvertLinks(pageList);

                    if (!hrefs.isEmpty() ) {
                        hrefs.forEach(href -> {
                            try {
                                Document advertDoc = Jsoup.connect(href).get();
                                Adverts advert = new Adverts();
                                populateModelData(form, advert);
                                advertParser.populate(advertDoc, advert);
                                if (databaseAdverts.stream().noneMatch(databaseAdvert -> databaseAdvert.getAdvertId().equals(advert.getAdvertId()))) {
                                    adverts.add(advert);
                                    advertsRepository.save(advert);
                                }
                            } catch (IOException e) {
                                LOG.info(e.getMessage());
                            }
                        });
                    }
                } catch (IOException e) {
                    LOG.info(e.getMessage());
                }
            }
        } catch (IOException e) {
            LOG.info(e.getMessage());
        }
        adverts.addAll(databaseAdverts);
        return adverts;
    }

    private int getNumberOfPages(Document doc) {
        Optional<Elements> elements = Optional.ofNullable(doc.getElementsByClass("page"));
        if (elements.isPresent() &&  elements.get().size() > 0 ) {
            OptionalInt max = elements.get().stream()
                    .map(Element::text)
                    .map(a -> a.replaceAll("[\\D]", ""))
                    .filter(f -> !StringUtils.isEmpty(f))
                    .mapToInt(Integer::valueOf)
                    .max();
            if (max.isPresent())
                return max.getAsInt();
        } else if (getAdvertLinks(doc).size() > 0  ){
            return 1;
        }
        return 0;
    }

    private void populateModelData(CarData form, Adverts advert) {
        advert.setMake(form.getMake());
        advert.setModel(form.getModel());
        advert.setVersion(form.getVersion());
    }

    private String getCarUrl(CarData form) {
        String url;
        if(StringUtils.isEmpty(form.getVersion())){
            url = BASE + form.getMake() + "/" + form.getModel() + END;
        } else {
            url = BASE + form.getMake() + "/" + form.getModel() + "/" + form.getVersion() + END;
        }
        return url;
    }

    private List<String> getAdvertLinks(Document doc) {
        Elements links = doc.getElementsByClass("offer-item__photo-link");
        return links.stream().map(a -> a.attributes()).map(a -> a.get("href")).collect(Collectors.toList());
    }

}
