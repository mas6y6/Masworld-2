package com.mas6y6.masworld.RecipeBook;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class RecipeBookUtils {
    public static final int[][] CRAFTING_SLOTS = {
            {10, 11, 12},
            {19, 20, 21},
            {28, 29, 30}
    };

    public static ItemStack getPlaceholderItem() {
        ItemStack itemStack = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        itemStack.editMeta(meta -> {
            meta.lore(Collections.emptyList());
            meta.itemName(Component.text(""));
        });
        return itemStack;
    }

    public static ItemStack getPageNumberIndicator(int current_page, int max_page) {
        ItemStack itemStack = new ItemStack(Material.COMPASS);
        itemStack.editMeta(meta -> {
            meta.lore(Collections.emptyList());
            meta.itemName(MiniMessage.miniMessage().deserialize(String.format("<!i><white>%d <dark_gray>/</dark_gray> %d</white>",current_page,max_page)));
        });
        return itemStack;
    }

    public static ItemStack getPreviousButton(Material material) {
        ItemStack itemStack = new ItemStack(material);
        itemStack.editMeta(meta -> {
            meta.lore(Collections.emptyList());
            meta.itemName(MiniMessage.miniMessage().deserialize("<!i><white><b><gray>⏴</gray></b> Previous Page</white>"));
        });
        itemStack.editPersistentDataContainer(pdc -> {
            NamespacedKey gui_id_key = new NamespacedKey("masworld","recipe_gui_id_key");

            pdc.set(gui_id_key, PersistentDataType.STRING,"previous_btn");
        });

        return itemStack;
    }

    public static ItemStack getNextbutton(Material material) {
        ItemStack itemStack = new ItemStack(material);
        itemStack.editMeta(meta -> {
            meta.lore(Collections.emptyList());
            meta.itemName(MiniMessage.miniMessage().deserialize("<!i><white>Next Page <b><gray>⏵</gray></b></white>"));
        });
        itemStack.editPersistentDataContainer(pdc -> {
            NamespacedKey gui_id_key = new NamespacedKey("masworld","recipe_gui_id_key");

            pdc.set(gui_id_key, PersistentDataType.STRING,"next_btn");
        });

        return itemStack;
    }

    public static ItemStack getBackButton(Material material) {
        ItemStack itemStack = new ItemStack(material);
        itemStack.editMeta(meta -> {
            meta.lore(Collections.emptyList());
            meta.itemName(MiniMessage.miniMessage().deserialize("<!i><red>← Back</red>"));
        });
        itemStack.editPersistentDataContainer(pdc -> {
            NamespacedKey gui_id_key = new NamespacedKey("masworld","recipe_gui_id_key");

            pdc.set(gui_id_key, PersistentDataType.STRING,"back_btn");
        });

        return itemStack;
    }

    public static ItemStack fixItem(ItemStack itemStack, int page_num, int num) {
        ItemStack clone = itemStack.clone();
        clone.editMeta(meta -> {
            List<Component> lore = new ArrayList<>();

            lore.add(MiniMessage.miniMessage().deserialize("<!i><green>ℹ Click to view recipe!</green>"));

            if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasLore()) {
                lore.addAll(Objects.requireNonNull(itemStack.getItemMeta().lore()));
            }

            meta.lore(lore);
        });

        clone.editPersistentDataContainer(pdc -> {
            NamespacedKey page_num_key = new NamespacedKey("masworld","recipe_book_page_num");
            NamespacedKey num_key = new NamespacedKey("masworld","recipe_book_num");

            pdc.set(page_num_key, PersistentDataType.INTEGER,page_num);
            pdc.set(num_key, PersistentDataType.INTEGER,num);
        });

        return clone;
    }

    public static ItemStack getRecipeTypeItem(Material material,String itemName) {
        ItemStack itemStack = new ItemStack(material);
        itemStack.editMeta(meta -> {
            meta.lore(Collections.emptyList());
            meta.itemName(MiniMessage.miniMessage().deserialize(itemName));
        });

        return itemStack;
    }
    public static ItemStack generateItemWithLore(Material material,String itemName,List<Component> lore) {
        ItemStack itemStack = new ItemStack(material);
        itemStack.editMeta(meta -> {
            meta.lore(lore);
            meta.itemName(MiniMessage.miniMessage().deserialize(itemName));
        });

        return itemStack;
    }

    public static ItemStack toDisplayItem(RecipeChoice choice) {
        if (choice instanceof RecipeChoice.ExactChoice exact) {
            return exact.getItemStack().clone();
        }

        if (choice instanceof RecipeChoice.MaterialChoice material) {
            return new ItemStack(material.getChoices().get(0));
        }

        return null;
    }

    public static String ticksToTimeString(int ticks) {
        int totalSeconds = ticks / 20;
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;

        StringBuilder sb = new StringBuilder();
        if (hours > 0) sb.append(hours).append("h ");
        if (minutes > 0 || hours > 0) sb.append(minutes).append("m ");
        sb.append(seconds).append("s");

        return sb.toString();
    }

}
