package com.mas6y6.masworld.RecipeBook;

import com.mas6y6.masworld.Masworld;
import io.papermc.paper.persistence.PersistentDataContainerView;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;

public class RecipeBookListener implements Listener {
    public RecipeBookManager recipeBookManager;

    public RecipeBookListener(RecipeBookManager recipeBookManager) {
        this.recipeBookManager = recipeBookManager;
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (event.getInventory().getHolder() instanceof RecipeBook recipeBook) {
            recipeBookManager.close(event.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        if (event.getInventory().getHolder() instanceof RecipeBook) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getInventory().getHolder() instanceof RecipeBook recipeBook) {
            event.setCancelled(true);
            if (event.getCurrentItem() == null) return;

            NamespacedKey gui_id_key = new NamespacedKey("masworld","recipe_gui_id_key");
            NamespacedKey page_num_key = new NamespacedKey("masworld","recipe_book_page_num");
            NamespacedKey recipe_num_key = new NamespacedKey("masworld","recipe_book_num");

            PersistentDataContainerView pdc = event.getCurrentItem().getPersistentDataContainer();
            if (pdc.has(gui_id_key)) {
                if (pdc.getOrDefault(gui_id_key, PersistentDataType.STRING, "").equals("back_btn")) {
                    recipeBook.buildSelectionMenu(0);
                }

                if (pdc.getOrDefault(gui_id_key, PersistentDataType.STRING, "").equals("next_btn")) {
                    recipeBook.nextPage();
                }

                if (pdc.getOrDefault(gui_id_key, PersistentDataType.STRING, "").equals("previous_btn")) {
                    recipeBook.previousPage();
                }
            } else if (pdc.has(recipe_num_key)) {
                recipeBook.selectRecipe(pdc.getOrDefault(page_num_key,PersistentDataType.INTEGER,0),pdc.getOrDefault(recipe_num_key,PersistentDataType.INTEGER,0));
            } else return;
        }
    }

    @EventHandler
    public void onRecipeBookOpen(PlayerInteractEvent event) {
        NamespacedKey special_effectKey = new NamespacedKey(Masworld.instance(), "special_effect");

        ItemStack item = event.getItem();
        if (item == null) return;

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        PersistentDataContainer container = meta.getPersistentDataContainer();

        if (!container.has(special_effectKey, PersistentDataType.STRING)) return;
        if (!Objects.equals(container.get(special_effectKey, PersistentDataType.STRING), "recipe_book")) return;

        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            this.recipeBookManager.open(event.getPlayer());
        }
    }
}
