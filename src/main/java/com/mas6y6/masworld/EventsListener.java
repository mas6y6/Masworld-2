package com.mas6y6.masworld;

import com.mas6y6.masworld.Items.GetAdminStick;
import com.mas6y6.masworld.Objects.CraftEngineUtils;
import io.papermc.paper.datacomponent.item.AttackRange;
import net.kyori.adventure.key.Key;
import net.momirealms.craftengine.bukkit.api.CraftEngineItems;
import net.momirealms.craftengine.core.item.CustomItem;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Barrel;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
//import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.LootGenerateEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import io.papermc.paper.event.packet.ClientTickEndEvent;
import io.papermc.paper.datacomponent.DataComponentTypes;

import java.util.List;
import java.util.UUID;
import java.util.Collection;

public class EventsListener implements Listener {
    public Masworld masworld;

    public EventsListener(Masworld plugin) {
        this.masworld = plugin;
    }

    private final NamespacedKey cekey = new NamespacedKey("masworld", "ce_item");
    private final NamespacedKey countKey = new NamespacedKey("masworld", "item_count");

    @EventHandler
    public void onLootGenerate(LootGenerateEvent event) {
        InventoryHolder holder = event.getInventoryHolder();
        if (!(holder instanceof Chest || holder instanceof Barrel)) return;

        List<ItemStack> loot = event.getLoot(); // get the loot list
        for (int i = 0; i < loot.size(); i++) {
            ItemStack item = loot.get(i);
            if (item == null) continue;

            String id = item.getPersistentDataContainer().get(cekey, PersistentDataType.STRING);
            int itemCount = item.getPersistentDataContainer()
                    .getOrDefault(countKey, PersistentDataType.BYTE, (byte) 1);
            if (id == null) continue;

            String[] idParts = id.split(":");
            if (idParts.length != 2) continue;

            CustomItem<ItemStack> customItem = CraftEngineItems.byId(
                    CraftEngineUtils.generateKey(idParts[0], idParts[1])
            );
            if (customItem == null) continue;

            ItemStack newItem = customItem.buildItemStack();
            newItem.setAmount(itemCount);

            loot.set(i, newItem);
        }
    }

    /*
    @EventHandler
    public void onAdminStickEquipped(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack adminStick = GetAdminStick.adminStick();
        ItemStack hand = player.getInventory().getItemInMainHand();

        AttributeInstance rangeAttr = player.getAttribute(Attribute.ENTITY_INTERACTION_RANGE);
        if (rangeAttr == null) return;

        Key reachKey = Key.key("masworld", "adminstick");
        NamespacedKey reachKey_nk = new NamespacedKey("masworld", "adminstick");
        AttributeModifier reachBonus = new AttributeModifier(reachKey_nk, 64.0, AttributeModifier.Operation.ADD_NUMBER);

        // Remove modifier if player is not holding the admin stick
        boolean holdingAdminStick = false;
        if (hand != null && hand.getType() == adminStick.getType()) {
            ItemMeta handIM = hand.getItemMeta();
            if (handIM != null) {
                NamespacedKey idKey = new NamespacedKey("masworld", "adminstick_id");
                PersistentDataContainer handPDC = handIM.getPersistentDataContainer();
                holdingAdminStick = "1040196".equals(handPDC.get(idKey, PersistentDataType.STRING));
            }
        }

        if (holdingAdminStick) {
            if (rangeAttr.getModifier(reachKey) == null) {
                rangeAttr.addModifier(reachBonus);
            }
        } else {
            if (rangeAttr.getModifier(reachKey) != null) {
                rangeAttr.removeModifier(reachKey);
            }
        }
    }
    */
    @EventHandler
    public void attackRangeValueSet(ClientTickEndEvent event) {
        Player player = event.getPlayer();
        ItemStack hand = player.getInventory().getItemInMainHand();
        AttributeInstance rangeAttr = player.getAttribute(Attribute.ENTITY_INTERACTION_RANGE);
        Collection<AttributeModifier> modifiers = rangeAttr.getModifiers();
        NamespacedKey reachKey_nk = new NamespacedKey("masworld", "reachbonus");
        if (rangeAttr == null) return;
        if (hand != null) {
            AttackRange itemrange = hand.getData(DataComponentTypes.ATTACK_RANGE);
            if (itemrange != null) {
                AttributeModifier reachBonus = new AttributeModifier(reachKey_nk, itemrange.maxCreativeReach(), AttributeModifier.Operation.ADD_NUMBER);
                rangeAttr.addModifier(reachBonus);
            } else {
                for (AttributeModifier mod : modifiers) {
                    if (mod.getKey().equals(reachKey_nk)) {
                        rangeAttr.removeModifier(reachKey_nk);
                    }
                }
            }
        }
    }
}
