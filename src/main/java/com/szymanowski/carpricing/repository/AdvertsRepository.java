package com.szymanowski.carpricing.repository;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AdvertsRepository extends CrudRepository<Adverts, Long> {

    List<Adverts> findByMakeAndModel(String make, String model);
    List<Adverts> findByMakeAndModelAndYear(String make, String model, Integer year);

}
