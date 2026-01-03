package com.mas6y6.masworld.Items;

import com.mas6y6.masworld.Items.Systems.PumpkinBlindness;
import com.mas6y6.masworld.Masworld;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.registrar.ReloadableRegistrarEvent;

public class Items {
    public AttributesHandle attributesHandle;
    public AdminStickHandler adminStickHandler;
    public PumpkinBlindness pumpkinBlindness;
    
    public Items() {
        Masworld.instance().getServer().getPluginManager().registerEvents(new WeaponListeners(this), Masworld.instance());
        Masworld.instance().getServer().getPluginManager().registerEvents(new EnchantmentListeners(this), Masworld.instance());

        this.pumpkinBlindness = new PumpkinBlindness();
        this.attributesHandle = new AttributesHandle();
        this.adminStickHandler = new AdminStickHandler();

    }

    public void commandsRegister(LiteralArgumentBuilder<CommandSourceStack> root, ReloadableRegistrarEvent<Commands> commands) {
        root.then(attributesHandle.buildCommands());
        commands.registrar().register(adminStickHandler.buildAdminStickCMD());
    }
}
