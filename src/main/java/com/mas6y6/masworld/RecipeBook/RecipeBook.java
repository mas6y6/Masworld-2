package com.mas6y6.masworld.RecipeBook;

import com.mas6y6.masworld.Masworld;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RecipeBook implements InventoryHolder {
    private final Inventory inventory;
    private final List<RecipeHolder> recipeMenus;

    public RecipeBook(List<RecipeHolder> recipeMenus) {
        this.inventory = Masworld.instance().getServer().createInventory(
                this,
                54,
                Component.text("Recipe Book")
        );

        this.recipeMenus = recipeMenus;


    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    public void close() {
        inventory.close();
    }
}
