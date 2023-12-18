package com.rpissarra.steps;

import com.rpissarra.exception.ResourceNotFoundException;
import com.rpissarra.ingredients.Ingredients;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StepsDAL {

    private final StepsRepository stepsRepository;

    public StepsDAL(StepsRepository stepsRepository) {
        this.stepsRepository = stepsRepository;
    }

    public void insertStep(Steps steps) {
        stepsRepository.save(steps);
    }

    public void insertAllSteps(List<Steps> stepsList) {
        stepsRepository.saveAll(stepsList);
    }

    public List<Steps> findAllStepsByRecipeId(Long id) {
        List<Steps> lstSteps = stepsRepository.findAllStepsByRecipeId(id);

        if (lstSteps.isEmpty()) {
            return null;
        }

        return lstSteps;
    }

    public void deleteAllSteps(List<Steps> lstSteps) {
        stepsRepository.deleteAll(lstSteps);
    }
}
