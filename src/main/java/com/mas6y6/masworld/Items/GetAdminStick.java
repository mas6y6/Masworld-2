package com.mas6y6.masworld.Items;

import com.mas6y6.masworld.Items.Attributes.Utils.SetWeaponDamage;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class GetAdminStick {
    public static ItemStack adminStick() {
        String adminStickName = "Admin Stick";
        ItemStack _adminStick = ItemType.STICK.createItemStack();
        _adminStick.setAmount(1);
        ItemMeta adminStickIM = _adminStick.getItemMeta();
        NamespacedKey key = new NamespacedKey("masworld", "adminstick_id");
        //NamespacedKey key = new NamespacedKey("masworld", "adminstick")
        PersistentDataContainer container = adminStickIM.getPersistentDataContainer();
        //container.set(key, PersistentDataType.BOOLEAN, true);
        container.set(key, PersistentDataType.STRING, "1040196");
        /*
        AttackRangeComponent adminStickIM_AR = adminStickIM.getAttackRange();
        if (adminStickIM_AR == null) {
            adminStickIM_AR = adminStickIM.getComponents().getAttackRange();
        }
        adminStickIM_AR.setValue(100);
        adminStickIM.setAttackRange(adminStickIM_AR);
        */
       /*
        AttackRange adminstickrange = AttackRange.attackRange()
            .minReach(1.0f)
            .maxReach(10.0f)
            .minCreativeReach(0.5f)
            .maxCreativeReach(100.0f)
            .build();
        _adminstick.setData(DataComponentTypes.ATTACK_RANGE, adminstickrange);
        */
        adminStickIM.setDisplayName(adminStickName);
        adminStickIM.setUnbreakable(true);
        adminStickIM.addEnchant(Enchantment.CHANNELING, 1, true);
        _adminStick.setItemMeta(adminStickIM);
        ItemStack adminStick = SetWeaponDamage.weaponDamage(_adminStick, 10000.0); //no "new" bc function is static
        return adminStick;
    }
}