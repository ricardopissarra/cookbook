package com.rpissarra.recipe;

import java.util.List;

public record RecipeUpdateRequest(
        String name,
        List<String> ingredients,
        String steps
) {
}
