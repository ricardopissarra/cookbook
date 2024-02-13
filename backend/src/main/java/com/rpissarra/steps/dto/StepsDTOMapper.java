package com.rpissarra.steps.dto;

import com.rpissarra.steps.Steps;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class StepsDTOMapper implements Function<Steps, StepsDTO> {
    @Override
    public StepsDTO apply(Steps steps) {
        return new StepsDTO(
                steps.getDescription()
        );
    }
}
