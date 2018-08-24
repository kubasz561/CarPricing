package com.szymanowski.carpricing.services;

import com.szymanowski.carpricing.Utils;
import com.szymanowski.carpricing.dto.CarData;
import com.szymanowski.carpricing.populator.AdvertParser;
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

@Service
public class SearchService {
    private static final Logger LOG = LoggerFactory.getLogger(SearchService.class);

    private static final String BASE = "https://www.otomoto.pl/osobowe/";
    private static final String END = "/?page=";
    private static final int MAX_PAGE_SIZE = 1;
    private static final boolean ONE_ADVERT_ONLY = false;

    @Autowired
    AdvertParser advertParser;

    @Autowired
    private AdvertsRepository advertsRepository;

    public List<Adverts> searchInDatabase(CarData form) {
        return form.getVersion() == null ? advertsRepository.findByMakeAndModelAndSaveDateAfter(form.getMake(), form.getModel(), Utils.getYearAgoDate())
                : advertsRepository.findByMakeAndModelAndVersionAndSaveDateAfter(form.getMake(),form.getModel(), form.getVersion(), Utils.getYearAgoDate());
    }

    // "https://www.otomoto.pl/osobowe/volkswagen/golf/v-2003-2009/?page=1";  page == 32 adverts
    public List<Adverts> search(CarData form) {
        List<Adverts> adverts = new ArrayList<>();
        List<Adverts> databaseAdverts = searchInDatabase(form);
        String url = getCarUrl(form);
        try {
            Document doc = Jsoup.connect(url + 1).get();
            int numberOfPages = getNumberOfPages(doc);
           // wait3();//TODO sprawdzenie ajax call > 3min
            int max = numberOfPages > MAX_PAGE_SIZE ? MAX_PAGE_SIZE : numberOfPages;//TODO zabezpieczenie na ilosc stron
            for (int i = 1; i <= max; ++i) {
                try {
                    Document pageList = Jsoup.connect(url + i).get();

                    List<String> hrefs = getAdvertLinks(pageList);

                    if (!hrefs.isEmpty() && !ONE_ADVERT_ONLY) {
                        hrefs.forEach(href -> {
                            sleep();//TODO randomizer
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
                    } else if (!hrefs.isEmpty()) {
                        //TEMPORARY FOR TEST
                        Document advertDoc = Jsoup.connect(hrefs.get(0)).get();
                        Adverts advert = new Adverts();
                        populateModelData(form, advert);
                        advertParser.populate(advertDoc, advert);
                        adverts.add(advert);
                        advertsRepository.save(advert);
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

    private void sleep() {
        Random r = new Random();
        int low = 1;
        int high = 4;
        int result = r.nextInt(high-low) + low;
        int ms = result * 500;
        try {
            Thread.sleep((long) (ms));
        } catch (InterruptedException e) {
            LOG.info(e.getMessage());
        }
    }
    private void wait3() {
        try {
            Thread.sleep((long) (180000));
        } catch (InterruptedException e) {
            LOG.info(e.getMessage());
        }
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
