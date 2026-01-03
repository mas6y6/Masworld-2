package com.mas6y6.masworld.PhoneDialog;

import com.mas6y6.masworld.Masworld;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PhoneManager {
    public Map<UUID,PlayerPhone> phones = new HashMap<>();

    public PhoneManager() {}

    public void open(Player player) {
        PlayerPhone phone = new PlayerPhone(player,this);
        phones.put(player.getUniqueId(),phone);
    }

    public void close(UUID uuid) {
        phones.remove(uuid);
        Masworld.instance().logger.info("Phone removed");
    }
}
