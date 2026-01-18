package com.mas6y6.masworld.PhoneDialog;

import com.earth2me.essentials.User;
import com.earth2me.essentials.api.Economy;
import com.earth2me.essentials.api.NoLoanPermittedException;
import com.mas6y6.masworld.Masworld;
import com.mas6y6.masworld.Objects.EssentialsUtils;
import com.mas6y6.masworld.Objects.TextSymbols;
import com.mas6y6.masworld.Objects.Utils;
import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.dialog.DialogResponseView;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.ess3.api.MaxMoneyException;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.dialog.DialogLike;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickCallback;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public class PlayerPhone {

    private static final MiniMessage MM = MiniMessage.miniMessage();

    private final Player player;
    private final PhoneManager phoneManager;

    public PlayerPhone(Player player, PhoneManager phoneManager) {
        this.player = player;
        this.phoneManager = phoneManager;
        player.showDialog(mainMenu());
    }

    /* -------------------------------------------------------------------------- */
    /* Helpers                                                                    */
    /* -------------------------------------------------------------------------- */

    private DialogBase.Builder base(String title) {
        return DialogBase.builder(Component.text(title))
                .canCloseWithEscape(true)
                .afterAction(DialogBase.DialogAfterAction.CLOSE);
    }

    private ActionButton closeButton() {
        return ActionButton.create(
                Component.text("Close"),
                Component.text("Click to close your phone"),
                300,
                DialogAction.staticAction(ClickEvent.callback(this::close))
        );
    }

    private void showInfo(Component text, String title, DialogLike backTo) {
        player.closeDialog();
        player.showDialog(infoScreen(text, title, backTo));
    }

    public void close(Audience audience) {
        Masworld.instance().logger.info("Phone closed");
        player.closeDialog();
        phoneManager.close(player.getUniqueId());
    }

    /* -------------------------------------------------------------------------- */
    /* Main Menu                                                                  */
    /* -------------------------------------------------------------------------- */

    public Dialog mainMenu() {
        return Dialog.create(builder -> builder.empty()
                .base(base("Phone - Main Menu")
                        .body(List.of(DialogBody.plainMessage(
                                MM.deserialize("""
                                        Welcome to the Masworld SMP!

                                        Click the buttons below to enter a menu.
                                        Or press ESC or the X button to close.
                                        """)
                        )))
                        .build())
                .type(DialogType.multiAction(List.of(
                        button("Bank Account", "Open bank account", () -> player.showDialog(bankAccount())),
                        button("Digital Recipe Book", "Open recipe book", () -> {
                            close(player);
                            Masworld.instance().recipeBookManager.open(player);
                        }),
                        button("Personal Vault", "Under construction", () -> player.showDialog(underConstruction())),
                        button("Homes", "Manage homes", () -> player.showDialog(homes()))
                )).exitAction(closeButton()).columns(1).build())
        );
    }

    /* -------------------------------------------------------------------------- */
    /* Bank Account                                                               */
    /* -------------------------------------------------------------------------- */

    public Dialog bankAccount() {
        BigDecimal bal = BigDecimal.valueOf(Masworld.instance().economy.getBalance(player));

        return Dialog.create(builder -> builder.empty()
                .base(base("Phone - Bank Account")
                        .body(List.of(DialogBody.plainMessage(
                                MM.deserialize("""
                                        Holder: %s

                                        Balance: %s
                                        Exact Balance: $%s

                                        %s
                                        """.formatted(
                                        player.getName(),
                                        Masworld.instance().economy.format(bal.doubleValue()),
                                        bal.toPlainString(),
                                        Utils.formatMoney(bal.doubleValue())
                                ))
                        )))
                        .build())
                .type(DialogType.multiAction(List.of(
                        button("Pay someone", "Send money", () -> player.showDialog(paySomeone())),
                        button("Withdraw", "Under construction", () -> player.showDialog(underConstruction())),
                        button("Back", "Main menu", () -> player.showDialog(mainMenu()))
                )).exitAction(closeButton()).columns(1).build())
        );
    }

    public Dialog paySomeone() {
        BigDecimal bal = BigDecimal.valueOf(Masworld.instance().economy.getBalance(player));

        return Dialog.create(builder -> builder.empty()
                .base(base("Phone - Pay Someone")
                        .body(List.of(DialogBody.plainMessage(
                                MM.deserialize("Balance: $" + bal.toPlainString())
                        )))
                        .inputs(List.of(
                                DialogInput.text("username", Component.text("Username")).build(),
                                DialogInput.text("amount", Component.text("Amount")).build()
                        ))
                        .build())
                .type(DialogType.multiAction(List.of(
                        ActionButton.create(
                                Component.text("Pay"),
                                Component.text("Confirm transaction"),
                                300,
                                DialogAction.customClick(this::handlePayment,
                                        ClickCallback.Options.builder().uses(1).build())
                        ),
                        button("Back", "Bank account", () -> player.showDialog(bankAccount()))
                )).exitAction(closeButton()).columns(1).build())
        );
    }

    private void handlePayment(DialogResponseView view, Audience audience) {
        BigDecimal amount;
        try {
            amount = new BigDecimal(view.getText("amount"));
            if (amount.signum() <= 0) throw new NumberFormatException();
        } catch (Exception e) {
            showInfo(MM.deserialize("<red>Invalid amount!</red>"), "Error", paySomeone());
            return;
        }

        String name = view.getText("username");
        if (name == null || name.isBlank()) {
            showInfo(MM.deserialize("<red>Username required!</red>"), "Error", paySomeone());
            return;
        }

        Player target = Bukkit.getPlayerExact(name);
        if (target == null) {
            showInfo(MM.deserialize("<red>Player not online!</red>"), "Error", paySomeone());
            return;
        }

        User from = EssentialsUtils.getEssentialUserPlayer(player);
        User to = EssentialsUtils.getEssentialUserPlayer(target);

        if (!Economy.hasEnough(from, amount)) {
            showInfo(MM.deserialize("<red>Insufficient funds!</red>"), "Error", paySomeone());
            return;
        }

        try {
            Economy.subtract(from, amount);
            Economy.add(to, amount);
        } catch (NoLoanPermittedException | MaxMoneyException e) {
            e.printStackTrace();
            player.showDialog(errorDialog());
            return;
        }

        showInfo(MM.deserialize("<green>Transaction successful!</green>"),
                "Success",
                bankAccount());
    }

    /* -------------------------------------------------------------------------- */
    /* Homes                                                                      */
    /* -------------------------------------------------------------------------- */

    public Dialog homes() {
        return homes(EssentialsUtils.getEssentialUserPlayer(player).getHomes());
    }

    public Dialog homes(List<String> homes) {
        List<ActionButton> buttons = new ArrayList<>();
        buttons.add(button("+ Add Home", "Create new home", () -> player.showDialog(newHome())));

        homes.forEach(home ->
                buttons.add(button(home, "Open " + home, () -> player.showDialog(openHome(home))))
        );

        buttons.add(button("Back", "Main menu", () -> player.showDialog(mainMenu())));

        return dialog("Phone - Homes", buttons);
    }

    public Dialog openHome(String home) {
        return dialog("Phone - Home - " + home, List.of(
                button("Teleport", "Go to home", () -> {
                    close(player);
                    player.sendMessage(TextSymbols.info("Teleporting to \"" + home + "\""));
                    player.teleport(EssentialsUtils.getEssentialUserPlayer(player).getHome(home));
                }),
                button("Delete", "Remove home", () -> {
                    try {
                        User user = EssentialsUtils.getEssentialUserPlayer(player);
                        user.delHome(home);
                        user.save();
                        player.showDialog(homes(user.getHomes()));
                    } catch (Exception e) {
                        e.printStackTrace();
                        player.showDialog(errorDialog());
                    }
                }),
                button("Back", "Homes list", () -> player.showDialog(homes()))
        ));
    }

    public Dialog newHome() {
        return Dialog.create(builder -> builder.empty()
                .base(base("Phone - Add Home")
                        .inputs(List.of(
                                DialogInput.text("name", Component.text("Home name")).build()
                        ))
                        .build())
                .type(DialogType.multiAction(List.of(
                        ActionButton.create(
                                Component.text("Add Home"),
                                Component.text("Save home"),
                                300,
                                DialogAction.customClick((view, aud) -> {
                                    String name = view.getText("name");
                                    if (name == null || name.isBlank()) {
                                        showInfo(MM.deserialize("<red>Name required!</red>"), "Error", newHome());
                                        return;
                                    }

                                    User user = EssentialsUtils.getEssentialUserPlayer(player);
                                    user.setHome(name, player.getLocation());
                                    user.save();

                                    player.showDialog(homes(user.getHomes()));
                                }, ClickCallback.Options.builder().uses(1).build())
                        ),
                        button("Back", "Homes list", () -> player.showDialog(homes()))
                )).exitAction(closeButton()).columns(1).build())
        );
    }

    /* -------------------------------------------------------------------------- */
    /* Generic Dialogs                                                            */
    /* -------------------------------------------------------------------------- */

    public Dialog infoScreen(Component text, String title, DialogLike back) {
        return dialog("Phone - " + title, List.of(
                button("Dismiss", "Go back", () -> player.showDialog(back))
        ), List.of(DialogBody.plainMessage(text)));
    }

    public Dialog errorDialog() {
        return dialog("Phone - Error", List.of(
                button("Back", "Main menu", () -> player.showDialog(mainMenu()))
        ), List.of(DialogBody.plainMessage(
                MM.deserialize("<red>Something went wrong.</red>")
        )));
    }

    public Dialog underConstruction() {
        return dialog("Phone - Under Construction", List.of(
                button("Back", "Main menu", () -> player.showDialog(mainMenu()))
        ), List.of(DialogBody.plainMessage(
                MM.deserialize("This feature is under construction!")
        )));
    }

    /* -------------------------------------------------------------------------- */
    /* Builders                                                                   */
    /* -------------------------------------------------------------------------- */

    private Dialog dialog(String title, List<ActionButton> buttons) {
        return dialog(title, buttons, List.of());
    }

    private Dialog dialog(String title, List<ActionButton> buttons, List<DialogBody> body) {
        return Dialog.create(builder -> builder.empty()
                .base(base(title).body(body).build())
                .type(DialogType.multiAction(buttons)
                        .exitAction(closeButton())
                        .columns(1)
                        .build())
        );
    }

    private ActionButton button(String name, String desc, Runnable action) {
        return ActionButton.create(
                Component.text(name),
                Component.text(desc),
                300,
                DialogAction.staticAction(ClickEvent.callback(cb -> {
                    player.closeDialog();
                    action.run();
                }))
        );
    }
}