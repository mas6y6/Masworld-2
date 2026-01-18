package com.mas6y6.masworld.Commands;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.EssentialsUpgrade;
import com.earth2me.essentials.IEssentials;
import com.earth2me.essentials.User;
import com.earth2me.essentials.api.Economy;
import com.earth2me.essentials.commands.WarpNotFoundException;
import com.earth2me.essentials.config.EssentialsConfiguration;
import com.earth2me.essentials.config.EssentialsUserConfiguration;
import com.earth2me.essentials.userstorage.IUserMap;
import com.mas6y6.masworld.Masworld;
import com.mas6y6.masworld.Objects.CraftEngineUtils;
import com.mas6y6.masworld.Objects.TextSymbols;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.momirealms.craftengine.bukkit.api.CraftEngineItems;
import net.momirealms.craftengine.core.plugin.CraftEngine;
import net.momirealms.craftengine.core.util.Key;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;

@SuppressWarnings("UnstableApiUsage")
public class TestCommands {
    public LiteralArgumentBuilder<CommandSourceStack> commands = Commands.literal("test");

    public void buildCommands(LiteralArgumentBuilder<CommandSourceStack> root) {
        commands.then(
                Commands.literal("phone")
                        .executes(this::dialogTestCommand)
        );

        commands.then(
                Commands.literal("ce_test")
                        .executes(this::craftEngineTest)
        );

        root.then(commands);
    }

    private int dialogTestCommand(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();

        if (!(source.getSender() instanceof Player player)) {
            source.getSender().sendMessage(TextSymbols.ERROR.append(
                    Component.text("You must be a Player!").color(NamedTextColor.RED)));
            return 0;
        }

        Masworld.instance().phoneManager.open(player);

        return 1;
    }

    private int craftEngineTest(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();

        if (!(source.getSender() instanceof Player player)) {
            source.getSender().sendMessage(TextSymbols.ERROR.append(
                    Component.text("You must be a Player!").color(NamedTextColor.RED)));
            return 0;
        }



        return 1;
    }
}
