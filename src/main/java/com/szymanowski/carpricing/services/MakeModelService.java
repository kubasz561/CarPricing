package com.szymanowski.carpricing.services;

import com.szymanowski.carpricing.repository.MakeModel;
import com.szymanowski.carpricing.repository.MakeModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Serwis odpowiada za pobranie z bazy danych informacji na temat dostępnych marek, modeli oraz wersji pojazdów.
 */
@Service
public class MakeModelService {
    @Autowired
    MakeModelRepository makeModelRepository;

    /**
     * Pobiera z bazy dostępne marki pojazdu
     * @return lista dostępnych marek pojazdu
     */
    public List<String> getAllMakes(){
         return StreamSupport.stream(makeModelRepository.findAll().spliterator(), false)
                 .map(MakeModel::getMake)
                 .distinct()
                 .collect(Collectors.toList());

    }

    /**
     * Pobiera z bazy dostępne modele pojazdu dla danej marki
     * @param make - marka pojazdu
     * @return lista dostępnych modeli dla podanej marki
     */
    public List<String> getModelsForMake(String make){
        return makeModelRepository.findByMake(make).stream()
                .map(MakeModel::getModel)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * Pobiera z bazy dostępne wersje pojazdu dla danego modelu i marki
     * @param make - marka pojazdu
     * @param model - model pojazdu
     * @return lista dostępnych wersji pojazdu dla podanego modelu i marki
     */
    public List<String> getVersionForMakeModel(String make, String model){
        Optional<String> version = makeModelRepository.findByMakeAndModel(make, model).stream()
                .findFirst()
                .map(MakeModel::getVersion);
        if(version.isPresent()){
            return Arrays.stream(version.get().split(",")).collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }

    }
}
