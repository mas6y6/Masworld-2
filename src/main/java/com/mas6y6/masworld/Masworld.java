package com.mas6y6.masworld;

import com.mas6y6.masworld.Commands.TestCommands;
import com.mas6y6.masworld.Commands.XPBottler;
import com.mas6y6.masworld.ItemEffects.ItemEffects;
import com.mas6y6.masworld.Items.Items;
import com.mas6y6.masworld.Objects.TextSymbols;
import com.mas6y6.masworld.PhoneDialog.PhoneManager;
import com.mas6y6.masworld.RecipeBook.RecipeBookManager;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Logger;

import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.YELLOW;

public final class Masworld extends JavaPlugin {
    public static Masworld instance;
    public Logger logger;
    public Configuration config;
    public Items items;
    public ItemEffects itemEffects;
    public XPBottler xpBottler;
    public RecipeBookManager recipeBookManager;
    public PhoneManager phoneManager;
    public TestCommands testCommands;

    public static Masworld instance() {
        return instance;
    }

    @Override
    public void onEnable() {
        logger = getLogger();
        logger.info("Masworld plugin startup in progress.");

        saveDefaultConfig();
        config = getConfig();

        String path = config.getString("items_directory", "items/");
        File items_dir = new File(path);
        if (!items_dir.isAbsolute()) {
            items_dir = new File(getDataFolder(), path);
        }

        if (!items_dir.exists()) {
            if (!items_dir.mkdirs()) {
                getLogger().severe("Failed to create effect directory at: " + items_dir.getAbsolutePath());
                getServer().getPluginManager().disablePlugin(this);
                return;
            }
        }

        instance = this;

        logger.info("Starting ItemEffects.");

        itemEffects = new ItemEffects(items_dir);
        try {
            itemEffects.loadEffects();
        } catch (Exception e) {
            this.getLogger().severe("Failed to get files for item effects." + e.getMessage());
            e.printStackTrace();
            this.getServer().getPluginManager().disablePlugin(this);
        }

        logger.info("Starting Items.");
        this.items = new Items();

        logger.info("Starting XPBottler.");
        this.xpBottler = new XPBottler();

        logger.info("Starting RecipeBookManager");
        this.recipeBookManager = new RecipeBookManager();

        this.phoneManager = new PhoneManager();

        this.testCommands = new TestCommands();

        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("masworld");
            commands.registrar().register(this.xpBottler.cmd.build());
            root.then(Commands.literal("reload").executes(this::pluginReloadcommmand));
            root.then(itemEffects.buildCommands());
            items.commandsRegister(root, commands);

            testCommands.buildCommands(root);

            commands.registrar().register(root.build());
        });

        getLogger().info("Registered Commands");

        getServer().getScheduler().runTaskTimer(this,() -> {
            for (Player player : getServer().getOnlinePlayers()) {
                if (player.hasPermission("masworld.itemeffect")) {
                    itemEffects.applyEffects(player);
                }
            }
        }, 0L,20L);

        getServer().getPluginManager().registerEvents(new EventsListener(this), this);
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    public int pluginReloadcommmand(CommandContext context) {
        CommandSourceStack source = (CommandSourceStack) context.getSource();
        if (source.getSender() instanceof Player player) {
            player.sendMessage(TextSymbols.WARNING.append(Component.text("Reloading Masworld").color(YELLOW)));
        }

        mainReload();

        if (source.getSender() instanceof Player player) {
            player.sendMessage(TextSymbols.SUCCESS.append(Component.text("Reload Complete!").color(GREEN)));
        }
        return 0;
    }

    public void mainReload() {
        getLogger().info("Reloading Masworld Plugin");
        try {
            itemEffects.loadEffects();
        } catch (Exception e) {
            this.getLogger().severe("Error reloading the ItemEffects: " + e.getMessage());
            e.printStackTrace();
        }

        for (Player player : getServer().getOnlinePlayers()) {
            if (player.hasPermission("masworld.itemeffect")) {
                itemEffects.applyEffects(player);
            }
        }

        recipeBookManager.reload();

        getLogger().info("Reload Complete");
    }
}
