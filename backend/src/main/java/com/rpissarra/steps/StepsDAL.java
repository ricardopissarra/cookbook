package com.rpissarra.steps;

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
}
