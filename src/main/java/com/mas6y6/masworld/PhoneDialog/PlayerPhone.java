package com.mas6y6.masworld.PhoneDialog;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.api.UserDoesNotExistException;
import com.mas6y6.masworld.Masworld;
import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public class PlayerPhone {
    private final Player player;
    private final PhoneManager phoneManager;

    public PlayerPhone(Player player,PhoneManager phoneManager) {
        this.player = player;
        this.phoneManager = phoneManager;

        player.showDialog(this.mainMenu());
    }

    public void close(Audience audience) {
        Masworld.instance().logger.info("Phone closed");
        player.closeDialog();
        phoneManager.close(player.getUniqueId());
    }

    public Dialog mainMenu() {
        Dialog dialog = Dialog.create(builder -> {
            DialogBase.Builder base = DialogBase.builder(Component.text("Phone - Main Menu"));
            base.canCloseWithEscape(true);
            base.afterAction(DialogBase.DialogAfterAction.CLOSE);

            List<DialogBody> bodyList = List.of(
                    DialogBody.plainMessage(
                            MiniMessage.miniMessage().deserialize("""
Welcome to the Masworld SMP!

Click the buttons below to enter a menu. Or press ESC or the X button to close.
""")
                    )
            );

            base.body(bodyList);

            List<ActionButton> actions = List.of(
                    ActionButton.create(Component.text("Player settings"),
                            Component.text("Click to open your player settings"),
                            300,
                            DialogAction.staticAction(ClickEvent.callback(callback -> {
                                        player.showDialog(underConstruction());
                                    })
                            )),
                    ActionButton.create(Component.text("Bank Account"),
                            Component.text("Click to open bank account"),
                            300,
                            DialogAction.staticAction(ClickEvent.callback(callback -> {
                                        player.showDialog(underConstruction());
                                    })
                            )),
                    ActionButton.create(Component.text("Digital Recipe Book"),
                            Component.text("Click to open digital recipe book"),
                            300,
                            DialogAction.staticAction(ClickEvent.callback(callback -> {
                                        this.close(callback);
                                        Masworld.instance().recipeBookManager.open(player);
                                    })
                            )),
                    ActionButton.create(Component.text("Personal Vault"),
                            Component.text("Click to open personal vault"),
                            300,
                            DialogAction.staticAction(ClickEvent.callback(callback -> {
                                        player.showDialog(underConstruction());
                                    })
                            )),
                    ActionButton.create(Component.text("Phone Settings"),
                            Component.text("Click to open phone settings"),
                            300,
                            DialogAction.staticAction(ClickEvent.callback(callback -> {
                                        player.showDialog(underConstruction());
                                    })
                            )),
                    ActionButton.create(Component.text("Homes"),
                            Component.text("Click to open homes"),
                            300,
                            DialogAction.staticAction(ClickEvent.callback(callback -> {
                                        player.showDialog(underConstruction());
                                    })
                            ))
            );

            builder.empty()
                    .base(base.build())
                    .type(DialogType.multiAction(actions)
                            .exitAction(ActionButton.create(Component.text("Close"),
                                    Component.text("Click to close your phone"),
                                    300,
                                    DialogAction.staticAction(ClickEvent.callback(this::close))))
                            .columns(1)
                            .build());
        });

        return dialog;
    }

    public Dialog bankAccount() {
        String balance = Masworld.instance().economy.format(Masworld.instance().economy.getBalance(player));
        String exactBalance = String.valueOf(Masworld.instance().economy.getBalance(player));

        Dialog dialog = Dialog.create(builder -> {
            DialogBase.Builder base = DialogBase.builder(Component.text("Phone - Bank Account"));
            base.canCloseWithEscape(true);
            base.afterAction(DialogBase.DialogAfterAction.CLOSE);

            List<DialogBody> bodyList = List.of(
                    DialogBody.plainMessage(
                            MiniMessage.miniMessage().deserialize(String.format("""
Holder: %s (%s)
Balance: %s
Exact Balance: %s

""",player.displayName(),player.getName(),balance,exactBalance))
                    )
            );

            base.body(bodyList);

            List<ActionButton> actions = List.of(
                    ActionButton.create(Component.text("Back"),
                            Component.text("Go back to main menu"),
                            300,
                            DialogAction.staticAction(ClickEvent.callback(callback -> {
                                        player.closeDialog();
                                        player.showDialog(mainMenu());
                                    })
                            )
                    )
            );

            builder.empty()
                    .base(base.build())
                    .type(DialogType.multiAction(actions)
                            .exitAction(ActionButton.create(Component.text("Close"),
                                    Component.text("Click to close your phone"),
                                    300,
                                    DialogAction.staticAction(ClickEvent.callback(this::close))))
                            .columns(1)
                            .build());
        });

        return dialog;
    }

    public Dialog errorDialog() {
        Dialog dialog = Dialog.create(builder -> {
            DialogBase.Builder base = DialogBase.builder(Component.text("Phone - Error"));
            base.canCloseWithEscape(true);
            base.afterAction(DialogBase.DialogAfterAction.CLOSE);

            List<DialogBody> bodyList = List.of(
                    DialogBody.plainMessage(
                            MiniMessage.miniMessage().deserialize("""
Something went wrong.
""")
                    )
            );

            base.body(bodyList);

            List<ActionButton> actions = List.of(
                    ActionButton.create(Component.text("Back"),
                            Component.text("Go back to main menu"),
                            300,
                            DialogAction.staticAction(ClickEvent.callback(callback -> {
                                        player.closeDialog();
                                        player.showDialog(mainMenu());
                                    })
                            )
                    )
            );

            builder.empty()
                    .base(base.build())
                    .type(DialogType.multiAction(actions)
                            .exitAction(ActionButton.create(Component.text("Close"),
                                    Component.text("Click to close your phone"),
                                    300,
                                    DialogAction.staticAction(ClickEvent.callback(this::close))))
                            .columns(1)
                            .build());
        });

        return dialog;
    }

    public Dialog underConstruction() {
        Dialog dialog = Dialog.create(builder -> {
            DialogBase.Builder base = DialogBase.builder(Component.text("Phone - Under Construction"));
            base.canCloseWithEscape(true);
            base.afterAction(DialogBase.DialogAfterAction.CLOSE);

            List<DialogBody> bodyList = List.of(
                    DialogBody.plainMessage(
                            MiniMessage.miniMessage().deserialize("""
This feature is under construction!
""")
                    )
            );

            base.body(bodyList);

            List<ActionButton> actions = List.of(
                    ActionButton.create(Component.text("Back"),
                            Component.text("Go back to main menu"),
                            300,
                            DialogAction.staticAction(ClickEvent.callback(callback -> {
                                    player.closeDialog();
                                    player.showDialog(mainMenu());
                                })
                            )
                    )
            );

            builder.empty()
                    .base(base.build())
                    .type(DialogType.multiAction(actions)
                            .exitAction(ActionButton.create(Component.text("Close"),
                                    Component.text("Click to close your phone"),
                                    300,
                                    DialogAction.staticAction(ClickEvent.callback(this::close))))
                            .columns(1)
                            .build());
        });

        return dialog;
    }
}
