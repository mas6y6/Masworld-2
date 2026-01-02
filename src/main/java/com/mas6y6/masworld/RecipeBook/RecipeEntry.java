package com.mas6y6.masworld.RecipeBook;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

public class RecipeEntry {
    private final ItemStack resultItem;
    private final NamespacedKey key;
    private final Recipe recipe;

    public RecipeEntry(NamespacedKey key) {
        this.recipe = Bukkit.getRecipe(key);
        this.resultItem = recipe.getResult();
        this.key = key;
    }

    public ItemStack getResultItem() {
        return resultItem;
    }

    public NamespacedKey getKey() {
        return key;
    }

    public Recipe getRecipe() {
        return recipe;
    }
}
