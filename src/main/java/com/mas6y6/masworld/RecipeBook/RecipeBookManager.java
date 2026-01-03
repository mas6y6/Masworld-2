package com.mas6y6.masworld.RecipeBook;



import com.mas6y6.masworld.Masworld;
import com.mas6y6.masworld.Objects.Utils;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.common.collect.Lists;

public class RecipeBookManager {
    public Map<UUID,RecipeBook> recipeBookGuis;
    private YamlConfiguration recipes_config;
    private YamlConfiguration config;
    private final File recipes_config_file;
    private final File config_file;

    public RecipeBookManager() {
        recipeBookGuis = new HashMap<>();

        Masworld.instance().saveResource("recipebook/recipes.yml",false);
        Masworld.instance().saveResource("recipebook/config.yml",false);

        this.recipes_config_file = new File(Masworld.instance().getDataFolder(),"recipebook/recipes.yml");
        this.config_file = new File(Masworld.instance().getDataFolder(),"recipebook/config.yml");

        this.recipes_config = YamlConfiguration.loadConfiguration(recipes_config_file);
        this.config = YamlConfiguration.loadConfiguration(config_file);

        Masworld.instance().getServer().getPluginManager().registerEvents(new RecipeBookListener(this), Masworld.instance());
    }

    public RecipeBook getRecipeBook(UUID uuid) {
        return recipeBookGuis.get(uuid);
    }

    public RecipeBook open(Player player) {
        List<List<String>> raw_recipes = Lists.partition(recipes_config.getStringList("recipes"),45);
        List<RecipeBookPage> recipe_pages = Lists.newArrayList();

        for (List<String> raw_recipe : raw_recipes) {
            RecipeBookPage recipe_page = new RecipeBookPage();

            for (String raw_recipe_name : raw_recipe) {
                recipe_page.addRecipe(new RecipeEntry(Utils.parseNamespacedKey(raw_recipe_name)));
            }

            recipe_pages.add(recipe_page);
        }

        RecipeBook recipeBook = new RecipeBook(recipe_pages,player);
        recipeBookGuis.put(player.getUniqueId(),recipeBook);
        return recipeBook;
    }

    public void close(UUID uuid) {
        recipeBookGuis.remove(uuid);
    }

    public void reload() {
        this.recipes_config = YamlConfiguration.loadConfiguration(recipes_config_file);
        this.config = YamlConfiguration.loadConfiguration(config_file);

        for (Map.Entry<UUID,RecipeBook> entry : this.recipeBookGuis.entrySet()){
            entry.getValue().close();
            this.recipeBookGuis.remove(entry.getKey());
        }
    }
}
