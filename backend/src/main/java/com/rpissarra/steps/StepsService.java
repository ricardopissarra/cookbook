package com.rpissarra.steps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StepsService {

    private final StepsRepository stepsRepository;

    public StepsService(StepsRepository stepsRepository) {
        this.stepsRepository = stepsRepository;
    }

    public Steps findStepsByRecipeId(long id) {
        return stepsRepository.findStepsByRecipeId(id);
    }

    public void save(Steps steps) {
        stepsRepository.save(steps);
    }

    public void delete(Steps stepsFromRecipe) {
        stepsRepository.delete(stepsFromRecipe);
    }
}
