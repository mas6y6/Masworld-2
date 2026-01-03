package com.mas6y6.masworld.Objects;

import com.mas6y6.masworld.Masworld;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SetWeaponDamage {
    public static ItemStack weaponDamage(ItemStack item, double amount) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) { return item; }
        NamespacedKey key = new NamespacedKey(Masworld.instance(), "admin_stick_damage");
        AttributeModifier modifier = new AttributeModifier(
            key,
            amount,
            Operation.ADD_NUMBER
        );
        meta.addAttributeModifier(Attribute.ATTACK_DAMAGE, modifier);
        item.setItemMeta(meta);
        return item;
    }
}