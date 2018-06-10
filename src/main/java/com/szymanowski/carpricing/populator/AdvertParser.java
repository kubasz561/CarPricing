package com.szymanowski.carpricing.populator;

import com.szymanowski.carpricing.repository.Adverts;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class AdvertParser {
    public void populate(Document doc, Adverts target){
        Elements title = doc.getElementsByClass("offer-title");
        Elements price = doc.getElementsByClass("offer-price__number");
        Elements items = doc.getElementsByClass("offer-params__item");
        Elements desc = doc.getElementsByClass("offer-description");
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

        String description = desc.get(0).childNodes().get(3).childNodes().stream()
                .filter(a-> a instanceof TextNode)
                .map(a->(TextNode)a)
                .map(TextNode::text)
                .collect(Collectors.joining(" "));

        target.setName(advertTitle);
        target.setAdvertId(advertID);
        target.setDate(new Date());
        target.setPrice(calculatePrice(advertPrice, isNettoPrice ));
        if(keys.containsKey("Rok produkcji"))
            target.setYear(Integer.parseInt(keys.get("Rok produkcji").replaceAll("[\\D]", "")));
        target.setColor(keys.get("Kolor"));
        if(keys.containsKey("Moc")) {
            target.setPower(Integer.parseInt(keys.get("Moc").replaceAll("[\\D]", "")));
        } else {
            //
        }
        if(keys.containsKey("Przebieg"))
            target.setMileage(Integer.parseInt(keys.get("Przebieg").replaceAll("[\\D]", "")));
        target.setNew("Nowy".equals(keys.get("Stan")));
        target.setType(keys.get("Typ"));
        target.setFirstOwner(keys.containsKey("Pierwszy właściciel"));
        target.setHadAccident(!keys.containsKey("Bezwypadkowy"));
        target.setFuel(keys.get("Rodzaj paliwa"));
        if(keys.containsKey("Pojemność skokowa")) {
            target.setEngineCapacity(Integer.parseInt(keys.get("Pojemność skokowa").substring(0,5).replaceAll("[\\D]", "")));
        } else {
            //
        }
        target.setDescription(description.length() > 4001 ? description.substring(0,4000) : description);
        //target.setDescription( description);
    }

    private Integer calculatePrice(String advertPrice, boolean isNettoPrice) {
        if(advertPrice!=null)
            advertPrice = advertPrice.split(",")[0];
        int price = Integer.parseInt(advertPrice.replaceAll("[\\D]", ""));
        return (int) (isNettoPrice ? price * 1.23 : price);
    }

}
