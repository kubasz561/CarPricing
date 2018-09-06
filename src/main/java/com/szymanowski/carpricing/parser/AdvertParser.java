package com.szymanowski.carpricing.parser;

import com.szymanowski.carpricing.repository.Adverts;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Klasa odpowiada za parsowanie kodu HTML i zapisywanie informacji o pojeździe w obiekcie typu Adverts.
 */
@Component
public class AdvertParser {

    /**
     * Metoda parsuje dokument DOM ogłoszenia zawarty w obiekcie doc i zapisuje go do obiektu target typu Adverts
     */
    public void populate(Document doc, Adverts target){
        Elements title = doc.getElementsByClass("offer-title");
        Elements price = doc.getElementsByClass("offer-price__number");
        Elements items = doc.getElementsByClass("offer-params__item");
        boolean isNettoPrice = false;
        if(doc.getElementsByClass("offer-price__details").size() > 0)
            isNettoPrice = doc.getElementsByClass("offer-price__details").get(0).text().toLowerCase().contains("netto");

        Long advertID = Long.valueOf(0);
        if(doc.getElementsByClass("offer-meta__value").size() > 1)
            advertID = Long.valueOf(doc.getElementsByClass("offer-meta__value").get(1).text());

        Map<String, String> keys =  new HashMap();

        String advertTitle = title.get(0).text();
        String advertPrice = price.get(0).text();

        items.forEach(item -> {
            Element node1 = (Element) item.childNodes().get(1);
            Element node3 = (Element) item.childNodes().get(3);

            keys.put(node1.text(),node3.text());
        });


        target.setName(advertTitle);
        target.setAdvertId(advertID);
        target.setSaveDate(new Date());
        target.setPrice(calculatePrice(advertPrice, isNettoPrice ));
        if(keys.containsKey("Rok produkcji"))
            target.setYear(Integer.parseInt(keys.get("Rok produkcji").replaceAll("[\\D]", "")));
        target.setColor(keys.get("Kolor"));
        if(keys.containsKey("Moc")) {
            target.setPower(Integer.parseInt(keys.get("Moc").replaceAll("[\\D]", "")));
        }
        if(keys.containsKey("Przebieg"))
            target.setMileage(Integer.parseInt(keys.get("Przebieg").replaceAll("[\\D]", "")));
        target.setType(keys.get("Typ"));
        target.setFirstOwner(keys.containsKey("Pierwszy właściciel"));
        target.setHadAccident(!keys.containsKey("Bezwypadkowy"));
        target.setFuel(keys.get("Rodzaj paliwa"));
        if(keys.containsKey("Pojemność skokowa")) {
            target.setEngineCapacity(Integer.parseInt(keys.get("Pojemność skokowa").substring(0,5).replaceAll("[\\D]", "")));
        }
    }

    private Integer calculatePrice(String advertPrice, boolean isNettoPrice) {
        if(advertPrice!=null)
            advertPrice = advertPrice.split(",")[0];
        int price = Integer.parseInt(advertPrice.replaceAll("[\\D]", ""));
        return (int) (isNettoPrice ? price * 1.23 : price);
    }

}
