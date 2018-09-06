package com.szymanowski.carpricing.repository;

import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

/**
 * Klasa udostępniająca operacje na tabeli Adverts
 */
public interface AdvertsRepository extends CrudRepository<Adverts, Long> {

    /**
     * Zapytanie znajdujące wszystkie ogłoszenia dla danej marki i modelu, które zostały zapisane w bazie nie wcześniej niż zadana data
     */
    List<Adverts> findByMakeAndModelAndSaveDateAfter(String make, String model, Date saveDate);

    /**
     * Zapytanie znajdujące wszystkie ogłoszenia dla danej marki, modelu oraz wersji, które zostały zapisane w bazie nie wcześniej niż zadana data
     */
    List<Adverts> findByMakeAndModelAndVersionAndSaveDateAfter(String make, String model, String version, Date saveDate);

}
