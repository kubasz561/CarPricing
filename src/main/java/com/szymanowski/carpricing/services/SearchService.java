package com.szymanowski.carpricing.services;

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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchService {
    private static final Logger LOG = LoggerFactory.getLogger(SearchService.class);

    private static final String BASE = "https://www.otomoto.pl/osobowe/";
    private static final String END = "/?page=";

    @Autowired
    AdvertParser advertParser;

    @Autowired
    private AdvertsRepository advertsRepository;

    // "https://www.otomoto.pl/osobowe/volkswagen/golf/v-2003-2009/?search%5Bbrand_program_id%5D%5B0%5D=&search%5Bcountry%5D=&page=";
    public List<Adverts> search(CarData form) {
        List<Adverts> adverts = new ArrayList<>();

        String url = getCarUrl(form);
        //TODO pobieranie page i wrzucanie do petli
        for (int i = 1; i < 2; ++i) {
            try {
                Document doc = Jsoup.connect(url+i).get();

                List<String> hrefs = getAdvertLinks(doc);

                int ONE_ADVERT_ONLY = 1;
                if (ONE_ADVERT_ONLY == 1) {
                    //TEMPORARY FOR TEST
                    Document advertDoc = Jsoup.connect(hrefs.get(0)).get();
                    Adverts advert = new Adverts();
                    populateModelData(form, advert);
                    advertParser.populate(advertDoc, advert);
                    adverts.add(advert);
                    advertsRepository.save(advert);
                } else {
                    hrefs.forEach(href -> {
                        try {
                            Document advertDoc = Jsoup.connect(href).get();
                            Adverts advert = new Adverts();
                            advertParser.populate(advertDoc, advert);
                            adverts.add(advert);
                            advertsRepository.save(advert);
                        } catch (IOException e) {
                            LOG.info(e.getMessage());
                        }
                    });
                }
            } catch (IOException e) {
                LOG.info(e.getMessage());
            }
        }
        return adverts;
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

    //https://www.otomoto.pl/osobowe/volkswagen/golf/v-2003-2009/?search%5Bbrand_program_id%5D%5B0%5D=&search%5Bcountry%5D=&page=3
    public List<Adverts> searchWithoutOpeningAdverts(CarData form) {
        String url = "https://www.otomoto.pl/osobowe/volkswagen/golf/v-2003-2009/?search%5Bbrand_program_id%5D%5B0%5D=&search%5Bcountry%5D=&" + form.getMake();

        try {
            Document doc = Jsoup.connect(url).get();

            Elements links = doc.getElementsByClass("offer-item__price");

            List<Adverts> adverts = new ArrayList<>();

            for (int i = 0; i < links.size(); i++) {
                List<String> params = doc.getElementsByClass("offer-item__params").get(i).childNodes().stream().filter(a -> a instanceof Element).map(a -> (Element) a).map(a -> a.text()).collect(Collectors.toList());

                if (links.get(i) != null && params.get(0) != null && params.get(1) != null) {
                    Adverts advert = new Adverts();
                    advert.setMake("Volkswagen");
                    advert.setModel("Golf V");
                    advert.setPrice(Integer.parseInt(links.get(i).text().replaceAll("[\\D]", "")));
                    advert.setYear(Integer.parseInt(params.get(0)));
                    advert.setMileage(Integer.parseInt(params.get(1).replaceAll("[\\D]", "")));
                    /*advert.setEngineCapacity(Integer.parseInt(params.get(2).substring(0, params.get(3).length() - 2).replaceAll("[\\D]", "")));
                    advert.setFuel(params.get(3));*/
                    adverts.add(advert);
                    advertsRepository.save(advert);

                }
            }

            return adverts;
        } catch (IOException e) {
            LOG.info(e.getMessage());
        }
        return null;
    }

    public List<Adverts> searchInDatabase(CarData form) {
        return advertsRepository.findByMakeAndModel(form.getMake(), form.getModel());
    }
}
