package com.mas6y6.masworld.Objects;

import com.earth2me.essentials.User;
import com.mas6y6.masworld.Masworld;
import net.ess3.api.IEssentials;
import org.bukkit.entity.Player;

public class EssentialsUtils {
    public static User getEssentialUserPlayer(Player player) {
        return new User(player, Masworld.instance().iEssentials);
    }

    public static IEssentials getEssentials() {
        return Masworld.instance().iEssentials;
    }
}
