package com.szymanowski.carpricing.services;

import com.szymanowski.carpricing.repository.MakeModel;
import com.szymanowski.carpricing.repository.MakeModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class MakeModelService {
    @Autowired
    MakeModelRepository makeModelRepository;

    public List<String> getAllMakes(){
         return StreamSupport.stream(makeModelRepository.findAll().spliterator(), false)
                 .map(MakeModel::getMake)
                 .collect(Collectors.toList());

    }
    public List<String> getModelsForMake(String make){
        return makeModelRepository.findByMake(make).stream()
                .map(MakeModel::getModel)
                .collect(Collectors.toList());
    }
    public List<String> getVersionForMakeModel(String make, String model){
        Optional<String> version = makeModelRepository.findByMakeAndModel(make, model).stream()
                .findFirst()
                .map(MakeModel::getVersion);
        if(version.isPresent()){
            return Arrays.stream(version.get().split(",")).collect(Collectors.toList());
        } else {
            return null;
        }

    }
}
