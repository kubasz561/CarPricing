package com.szymanowski.carpricing.repository;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Klasa udostępniająca operacje na tabeli MakeModel
 */
public interface MakeModelRepository extends CrudRepository<MakeModel, Long> {

    /**
     * Zapytanie znajdujące wszystkie rekordy dla danej marki
     */
    List<MakeModel> findByMake(String make);

    /**
     * Zapytanie znajdujące wszystkie rekordy dla danej marki oraz modelu
     */
    List<MakeModel> findByMakeAndModel(String make, String model);
}
