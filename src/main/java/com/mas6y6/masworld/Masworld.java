package com.mas6y6.masworld;

import org.bukkit.plugin.java.JavaPlugin;

public final class Masworld extends JavaPlugin {
    public static Masworld instance;

    public static Masworld getInstance() {
        return instance;
    }


    @Override
    public void onEnable() {

        instance = this;
    }

    @Override
    public void onDisable() {

        instance = null;
    }
}
