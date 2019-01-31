package com.mcbaka.bakacraft.Modules.Survival.DivineWrathHelper;

import org.spongepowered.api.entity.living.player.Player;

import java.time.LocalDateTime;
import java.util.HashMap;

public class PlayerDeadManager {
    public static HashMap<Player, LocalDateTime> restrictRecord = new HashMap<>();

    public static boolean PlayerIsInRestrict(Player player) {
        return restrictRecord.containsKey(player) && LocalDateTime.now().isAfter(restrictRecord.get(player).plusMinutes(5));
    }

    public static void RemovePlayerRestrict(Player player) {
        restrictRecord.remove(player);
    }

    public static void SetPlayerRestrict(Player player) {
        restrictRecord.put(player, LocalDateTime.now());
    }
}
