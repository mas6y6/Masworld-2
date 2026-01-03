package com.mas6y6.masworld.Items;

import com.mas6y6.masworld.Masworld;
import com.mas6y6.masworld.Objects.TextSymbols;
import com.mas6y6.masworld.Items.Attributes.SpecialEffect;
import com.mas6y6.masworld.Items.Attributes.WeaponDamage;
import com.mas6y6.masworld.Items.Attributes.*;
import com.mojang.brigadier.arguments.*;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;

public class AdminStickHandler {
    public LiteralArgumentBuilder<CommandSourceStack> adminStickCMD = Commands.literal("iwantadminstick");

    public LiteralArgumentBuilder<CommandSourceStack> buildAdminStickCMD() {
        adminStickCMD.executes(ctx -> {
            CommandSourceStack sender = ctx.getSource();
            ItemStack adminStick = new GetAdminStick().adminStick();
            if (sender.getExecutor() instanceof Player player) {
                player.getInventory().addItem(adminStick);

                player.sendMessage(TextSymbols.SUCCESS.append(
                        Component.text("BEHOLD: ")
                                .color(NamedTextColor.GREEN)
                                .append(Component.text("admin stick")
                                        .color(NamedTextColor.BLUE))
                ));
            }
            return 1;
        });
        return adminStickCMD;
    }
}