package com.mcbaka.bakacraft.Util;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;

import java.util.Collection;
import java.util.Optional;

public class GameServerHelper {
    public static Optional<Player> FindPlayerOnlineByName(String name) {
        Collection<Player> players = Sponge.getServer().getOnlinePlayers();
        String lowerCaseName = name.toLowerCase();
        for (Player player : players) {
            if (player.getName().toLowerCase().equals(lowerCaseName)) {
                return Optional.of(player);
            }
        }
        return Optional.empty();
    }
}
