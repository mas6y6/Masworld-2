package com.mas6y6.masworld.PhoneDialog;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PhoneListener implements Listener {
    private final PhoneManager phoneManager;

    public PhoneListener(PhoneManager phoneManager) {
        this.phoneManager = phoneManager;
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        this.phoneManager.close(event.getPlayer().getUniqueId());
        this.phoneManager.phones.remove(event.getPlayer().getUniqueId());
    }
}
