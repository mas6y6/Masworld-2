package com.mas6y6.masworld.RecipeBook;

import java.util.ArrayList;
import java.util.List;

public class RecipeBookPage {
    private final List<RecipeEntry> recipes = new ArrayList<>();

    public void addRecipe(RecipeEntry recipe_entry) {
        recipes.add(recipe_entry);
    }

    public RecipeEntry getRecipeEntry(int index) {
        return recipes.get(index);
    }
}
