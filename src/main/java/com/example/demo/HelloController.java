package com.example.demo;

import com.example.demo.dto.SearchForm;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.logging.Logger;

@RequestMapping("/api")
@RestController
public class HelloController {

    private static final Logger LOG = Logger.getLogger("HelloController");
    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return "Hello, World!";
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String search(SearchForm form) {

        String url = "https://www.otomoto.pl/osobowe/"+form.getMarka()+"/"+form.getModel()+"/?search%5Bnew_used%5D=on";
        try{
            Document doc = Jsoup.connect(url).get();
            Elements prices = doc.getElementsByClass("offer-price__number");
            prices.first();
        } catch (IOException e ) {
            LOG.info(e.getMessage());
        }

        return form.getMarka() + form.getModel();
    }
}