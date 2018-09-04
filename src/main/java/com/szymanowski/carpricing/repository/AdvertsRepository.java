package com.szymanowski.carpricing.repository;

import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface AdvertsRepository extends CrudRepository<Adverts, Long> {

    List<Adverts> findByMakeAndModelAndSaveDateAfter(String make, String model, Date saveDate);
    List<Adverts> findByMakeAndModelAndVersionAndSaveDateAfter(String make, String model, String version, Date saveDate);

}
