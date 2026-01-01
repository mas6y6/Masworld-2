package com.mas6y6.masworld.Items;

import com.mas6y6.masworld.Items.Systems.PumpkinBlindness;
import com.mas6y6.masworld.Masworld;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.registrar.ReloadableRegistrarEvent;
import org.jetbrains.annotations.NotNull;

public class Items {
    public AttributesHandle attributesHandle;

    public PumpkinBlindness pumpkinBlindness;
    
    public Items() {
        Masworld.instance().getServer().getPluginManager().registerEvents(new WeaponListeners(this), Masworld.instance());
        Masworld.instance().getServer().getPluginManager().registerEvents(new EnchantmentListeners(this), Masworld.instance());

        this.pumpkinBlindness = new PumpkinBlindness();
        this.attributesHandle = new AttributesHandle();
    }

    public void commandsRegister(ReloadableRegistrarEvent<@NotNull Commands> commandEvent) {
        commandEvent.registrar().register(attributesHandle.buildCommands().build());
    }
}
