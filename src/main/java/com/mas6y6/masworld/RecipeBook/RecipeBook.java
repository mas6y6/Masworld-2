package com.mas6y6.masworld.RecipeBook;

import com.mas6y6.masworld.Masworld;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.mas6y6.masworld.RecipeBook.RecipeBookUtils.CRAFTING_SLOTS;

public class RecipeBook implements InventoryHolder {
    private final Inventory inventory;
    private final List<RecipeBookPage> recipeMenus;
    private final Player player;
    private String menu_id;
    private int menu_page;
    private int max_page;
    private RecipeBookPage current_page;

    public RecipeBook(List<RecipeBookPage> recipeMenus, Player player) {
        this.inventory = Masworld.instance().getServer().createInventory(
                this,
                54,
                Component.text("Recipe Book")
        );

        this.recipeMenus = recipeMenus;
        this.player = player;
        this.menu_page = 0;
        this.max_page = recipeMenus.size();
        this.menu_id = "selection";

        buildSelectionMenu(menu_page);

        player.openInventory(getInventory());
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    public void buildSelectionMenu(int menu_page) {
        inventory.clear();

        this.menu_id = "selection";
        this.menu_page = menu_page;

        /* Menu Interaction */

        inventory.setItem(45,RecipeBookUtils.getPreviousButton(Material.PAPER));
        inventory.setItem(46,RecipeBookUtils.getPlaceholderItem());
        inventory.setItem(47,RecipeBookUtils.getPlaceholderItem());
        inventory.setItem(48,RecipeBookUtils.getPlaceholderItem());
        inventory.setItem(49,RecipeBookUtils.getPageNumberIndicator(this.menu_page + 1,this.max_page));
        inventory.setItem(50,RecipeBookUtils.getPlaceholderItem());
        inventory.setItem(51,RecipeBookUtils.getPlaceholderItem());
        inventory.setItem(52,RecipeBookUtils.getPlaceholderItem());
        inventory.setItem(53,RecipeBookUtils.getNextbutton(Material.PAPER));

        /* Menu Interaction */

        this.current_page = this.recipeMenus.get(this.menu_page);

        for (int i = 0; i < current_page.getRecipes().size(); i++) {
            RecipeEntry entry = current_page.getRecipeEntry(i);
            inventory.setItem(i,RecipeBookUtils.fixItem(entry.getResultItem(),this.menu_page,i));
        }
    }

    public void nextPage() {
        if (!(this.menu_page == this.max_page - 1)) {
            buildSelectionMenu(this.menu_page + 1);
        }
    }

    public void previousPage() {
        if (!(this.menu_page == 0)) {
            buildSelectionMenu(this.menu_page - 1);
        }
    }

    public void close() {
        inventory.close();
    }

    public String getMenu_id() {
        return this.menu_id;
    }

    public void selectRecipe(int recipe_page, int recipe_num) {
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i,RecipeBookUtils.getPlaceholderItem());
        }

        this.menu_id = "recipe";

        inventory.setItem(49,RecipeBookUtils.getBackButton(Material.BARRIER));

        Recipe recipe = this.recipeMenus.get(recipe_page).getRecipeEntry(recipe_num).getRecipe();

        switch (recipe) {
            case ShapedRecipe shapedRecipe -> renderCraftingShaped(shapedRecipe);
            case ShapelessRecipe shapelessRecipe -> renderCraftingShapeless(shapelessRecipe);
            case CookingRecipe<?> cookingRecipe -> renderCookingRecipe(cookingRecipe);
            case StonecuttingRecipe stonecuttingRecipe -> renderStoneCutting(stonecuttingRecipe);
            case SmithingTransformRecipe smithingTransformRecipe -> renderSmithingRecipe(smithingTransformRecipe);
            case SmithingTrimRecipe smithingTransformRecipe -> renderSmithingRecipe(smithingTransformRecipe);

            default -> {
                // ignore
            }
        }
    }

    public void renderCraftingShaped(ShapedRecipe shapedRecipe) {
        RecipeChoice[][] grid = new RecipeChoice[3][3];
        Map<Character, RecipeChoice> map = shapedRecipe.getChoiceMap();
        String[] shape = shapedRecipe.getShape();

        for (int row = 0; row < shape.length; row++) {
            String line = shape[row];
            for (int col = 0; col < line.length(); col++) {
                char key = line.charAt(col);
                if (key != ' ') {
                    grid[row][col] = map.get(key);
                }
            }
        }

        inventory.setItem(22,RecipeBookUtils.getRecipeTypeItem(Material.CRAFTING_TABLE,"<!i><white><gold>Crafting</gold> <dark_gray>-</dark_gray> Shaped</white>"));

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                int slot = CRAFTING_SLOTS[row][col];
                RecipeChoice choice = grid[row][col];

                if (choice == null) {
                    inventory.setItem(slot, null);
                    continue;
                }

                ItemStack display = RecipeBookUtils.toDisplayItem(choice);
                inventory.setItem(slot, display);
            }
        }

        inventory.setItem(24, shapedRecipe.getResult());
    }

    public void renderCraftingShapeless(ShapelessRecipe shapelessRecipe) {
        List<RecipeChoice> ingredients = shapelessRecipe.getChoiceList();

        List<Integer> flatSlots = new ArrayList<>();

        for (int row = 0; row < CRAFTING_SLOTS.length; row++) {
            for (int col = 0; col < CRAFTING_SLOTS[row].length; col++) {
                flatSlots.add(CRAFTING_SLOTS[row][col]);
            }
        }

        for (int i : flatSlots) {
            inventory.setItem(i,null);
        }

        int index = 0;
        for (RecipeChoice choice : ingredients) {
            if (index >= 9) break;

            int row = index / 3;
            int col = index % 3;
            int slot = CRAFTING_SLOTS[row][col];

            ItemStack display = RecipeBookUtils.toDisplayItem(choice);
            inventory.setItem(slot, display);

            index++;
        }

        inventory.setItem(
                22,
                RecipeBookUtils.getRecipeTypeItem(
                        Material.CRAFTING_TABLE,
                        "<!i><white><gold>Crafting</gold> <dark_gray>-</dark_gray> Shapeless</white>"
                )
        );

        inventory.setItem(24, shapelessRecipe.getResult().clone());
    }

    public void renderCookingRecipe(CookingRecipe<?> recipe) {
        inventory.setItem(11, RecipeBookUtils.toDisplayItem(recipe.getInputChoice()));

        Material furnanceType = Material.FURNACE;

        switch (recipe) {
            case BlastingRecipe blastingRecipe -> {
                furnanceType = Material.BLAST_FURNACE;
                inventory.setItem(22, RecipeBookUtils.getRecipeTypeItem(furnanceType,
                        "<!i><white>Cooking <dark_gray>-</dark_gray> Blasting</white>"));
            }
            case CampfireRecipe campfireRecipe -> {
                furnanceType = Material.CAMPFIRE;
                inventory.setItem(22, RecipeBookUtils.getRecipeTypeItem(furnanceType,
                        "<!i><white>Cooking <dark_gray>-</dark_gray> Campfire</white>"));
            }
            case SmokingRecipe smokingRecipe -> {
                furnanceType = Material.SMOKER;
                inventory.setItem(22, RecipeBookUtils.getRecipeTypeItem(furnanceType,
                        "<!i><white>Cooking <dark_gray>-</dark_gray> Smoking</white>"));
            }
            default -> {
                inventory.setItem(22, RecipeBookUtils.getRecipeTypeItem(furnanceType,
                        "<!i><white>Cooking <dark_gray>-</dark_gray> Furnace</white>"));
            }
        }

        inventory.setItem(29, RecipeBookUtils.generateItemWithLore(
                Material.COAL,
                String.format("<!i><gold>%s</gold>",RecipeBookUtils.ticksToTimeString(recipe.getCookingTime())),
                List.of(
                        MiniMessage.miniMessage().deserialize(String.format("<gray>Ticks: <white>%s",recipe.getCookingTime()))
                )
        ));

        inventory.setItem(24, recipe.getResult().clone());
    }

    public void renderStoneCutting(StonecuttingRecipe recipe) {
        inventory.setItem(20,RecipeBookUtils.toDisplayItem(recipe.getInputChoice()));

        inventory.setItem(22, RecipeBookUtils.getRecipeTypeItem(Material.STONECUTTER,
                "<!i><gray>Stonecutting</gray>"));

        inventory.setItem(24,recipe.getResult().clone());
    }

    public void renderSmithingRecipe(SmithingRecipe recipe) {
        if (recipe instanceof SmithingTransformRecipe transform) {
            inventory.setItem(19, RecipeBookUtils.toDisplayItem(transform.getBase()).clone());
            inventory.setItem(20, RecipeBookUtils.toDisplayItem(transform.getAddition()).clone());
            inventory.setItem(24, transform.getResult().clone());
            inventory.setItem(22, RecipeBookUtils.getRecipeTypeItem(
                    Material.SMITHING_TABLE,
                    "<!i><white>Smithing - <light_purple>Upgrade</light_purple></white>"
            ));
        } else if (recipe instanceof SmithingTrimRecipe trim) {
            inventory.setItem(19, RecipeBookUtils.toDisplayItem(trim.getBase()).clone());
            inventory.setItem(20, RecipeBookUtils.toDisplayItem(trim.getAddition()).clone());
            inventory.setItem(24, trim.getResult().clone());
            inventory.setItem(22, RecipeBookUtils.getRecipeTypeItem(
                    Material.SMITHING_TABLE,
                    "<!i><white>Smithing - </gold>Trim</gold></white>"
            ));
        }
    }
}