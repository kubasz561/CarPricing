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

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchService {
    private static final Logger LOG = LoggerFactory.getLogger(SearchService.class);

    private static final String BASE = "https://www.otomoto.pl/osobowe/";
    private static final String YEAR_MID = "?search%5Bfilter_float_year%3Ato%5D=";
    private static final String END = "search%5Bnew_used%5D=on";

    @Autowired
    AdvertParser advertParser;
    @Autowired
    private AdvertsRepository advertsRepository;

    //https://www.otomoto.pl/osobowe/bmw/seria-3/?search%5Bnew_used%5D=on;
    //https://www.otomoto.pl/osobowe/bmw/seria-3/od-2010/?search%5Bfilter_float_year%3Ato%5D=2010&search%5Bnew_used%5D=on
    public List<String> search(CarData form){
        String url;
        if (form.getYear() != null) {
            url = BASE + form.getMarka() + "/" + form.getModel() + "/od-" + form.getYear() + YEAR_MID + form.getYear() + "$" + END;
        } else {
            url = BASE + form.getMarka() + "/" + form.getModel() + "/?" + END;
        }

        try {
            Document doc = Jsoup.connect(url).get();

            Elements links = doc.getElementsByClass("offer-item__photo-link");
            List<String> hrefs = links.stream().map(a->a.attributes()).map(a->a.get("href")).collect(Collectors.toList());

            int ONE_ADVERT_ONLY = 1;
            if(ONE_ADVERT_ONLY == 1) {
                //TEMPORARY FOR TEST
                Document advertDoc = Jsoup.connect(hrefs.get(0)).get();
                Adverts advert = new Adverts();
                advertParser.populate(advertDoc, advert);
                advertsRepository.save(advert);
            } else {
                hrefs.forEach(href -> {
                    try {
                        Document advertDoc = Jsoup.connect(href).get();
                        Adverts advert = new Adverts();
                        advertParser.populate(advertDoc, advert);
                        advertsRepository.save(advert);
                    } catch (IOException e) {
                        LOG.info(e.getMessage());
                    }
                });
            }
            //test response
            Elements prices = doc.getElementsByClass("offer-price__number");
            return prices.stream().map(Element::text).collect(Collectors.toList());
        } catch (IOException e) {
            LOG.info(e.getMessage());
        }
        return null;
    }
}
