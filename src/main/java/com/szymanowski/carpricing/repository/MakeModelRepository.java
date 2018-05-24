package com.szymanowski.carpricing.repository;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MakeModelRepository extends CrudRepository<MakeModel, Long> {

    List<MakeModel> findByMake(String make);
    List<MakeModel> findByMakeAndModel(String make, String model);
}
