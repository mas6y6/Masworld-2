package com.mas6y6.masworld.Commands;

import com.mas6y6.masworld.Objects.TextSymbols;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

@SuppressWarnings("UnstableApiUsage")
public class TestCommands {
    public LiteralArgumentBuilder<CommandSourceStack> commands = Commands.literal("test");

    public void buildCommands(LiteralArgumentBuilder<CommandSourceStack> root) {
        root.then(commands);

        root.then(
                Commands.literal("dialog_test")
                        .executes(this::dialogTestCommand)
        );
    }

    private int dialogTestCommand(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();

        if (!(source.getSender() instanceof Player player)) {
            source.getSender().sendMessage(TextSymbols.ERROR.append(
                    Component.text("You must be a Player!").color(NamedTextColor.RED)));
            return 0;
        }

        Dialog dialog = Dialog.create(builder -> {
            DialogBase.Builder base = DialogBase.builder(Component.text("Title"));



            builder.empty()
                    .base(base.build())
                    .type(DialogType.multiAction());
        });

        player.showDialog(dialog);

        return 1;
    }
}
