package com.szymanowski.carpricing.populator;

import com.szymanowski.carpricing.repository.Adverts;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class AdvertParser {
    public void populate(Document doc, Adverts target){
        Elements items = doc.getElementsByClass("offer-params__item");
        Elements desc = doc.getElementsByClass("offer-description");
        fillPrices(doc, target);
        Map<String, String> keys =  new HashMap();
        /*items.forEach(item -> {
            Element node = item.childNodes().get(1);

            keys.put(node.text(),item.childNodes().get(3).text())
        });

        String description = desc.get(0).childNodes().get(3).childNodes().stream().map(a->a.text()).collect(Collectors.joining(", "));
*/
        target.setMake("test");
        //target.setPrice();
    }
    private void fillPrices(Document doc, Adverts target){

    }
}
