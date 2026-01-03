package com.mas6y6.masworld.Items;

import com.mas6y6.masworld.Objects.TextSymbols;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;

public class AdminStickHandler {
    public LiteralArgumentBuilder<CommandSourceStack> adminStickCMD = Commands.literal("iwantadminstick");

    public LiteralCommandNode<CommandSourceStack> buildAdminStickCMD() {
        adminStickCMD.executes(ctx -> {
            CommandSourceStack sender = ctx.getSource();
            ItemStack adminStick = GetAdminStick.adminStick();
            if (sender.getExecutor() instanceof Player player) {
                if (!(player.hasPermission("masworld.admin"))) {
                    player.sendMessage(TextSymbols.error("Your not a admin!"));
                    return 0;
                }

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
        return adminStickCMD.build();
    }
}