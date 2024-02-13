package com.rpissarra.recipe;

import java.util.List;

public record RecipeRegistrationRequest(
        String name,
        List<String> ingredients,
        String steps
) {
}
