package com.mas6y6.masworld.Commands;

import com.mas6y6.masworld.Objects.TextSymbols;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public class TestCommands {
    public LiteralArgumentBuilder<CommandSourceStack> commands = Commands.literal("test");

    public void buildCommands(LiteralArgumentBuilder<CommandSourceStack> root) {
        commands.then(
                Commands.literal("dialog_test")
                        .executes(this::dialogTestCommand)
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

        Dialog dialog = Dialog.create(builder -> {
            DialogBase.Builder base = DialogBase.builder(Component.text("Phone"));
            base.canCloseWithEscape(true);
            base.pause(true);

            List<ActionButton> actions = List.of(
                    ActionButton.create(Component.text("Bank Account"),
                            Component.text("Click to open bank account"),
                            300,
                            DialogAction.staticAction(ClickEvent.runCommand("tellraw @a {text:\"thing\"}"))),
                    ActionButton.create(Component.text("Bank Account"),
                            Component.text("Click to open bank account"),
                            300,
                            DialogAction.staticAction(ClickEvent.runCommand("tellraw @a {text:\"thing1\"}"))),
                    ActionButton.create(Component.text("Bank Account"),
                            Component.text("Click to open bank account"),
                            300,
                            DialogAction.staticAction(ClickEvent.runCommand("tellraw @a {text:\"thing2\"}"))),
                    ActionButton.create(Component.text("Bank Account"),
                            Component.text("Click to open bank account"),
                            300,
                            DialogAction.staticAction(ClickEvent.runCommand("tellraw @a {text:\"thing3\"}")))
            );

            builder.empty()
                    .base(base.build())
                    .type(DialogType.multiAction(actions)
                            .columns(1)
                            .build());
        });

        player.showDialog(dialog);

        return 1;
    }
}
