package com.mcbaka.bakacraft.Modules.Survival.DivineWrathHelper;

import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.PotionEffectData;
import org.spongepowered.api.effect.potion.PotionEffect;
import org.spongepowered.api.effect.potion.PotionEffectTypes;
import org.spongepowered.api.entity.living.player.Player;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Set;

public class PlayerDeadManager {
    public static HashMap<Player, LocalDateTime> restrictRecord = new HashMap<>();

    public static boolean PlayerIsInRestrict(Player player) {
        return restrictRecord.containsKey(player) && LocalDateTime.now().isAfter(restrictRecord.get(player).plusMinutes(5));
    }

    public static Set<Player> GetAllRestrictedPlayer() {
        return restrictRecord.keySet();
    }

    public static void RemovePlayerRestrict(Player player) {
        if (!restrictRecord.containsKey(player)) return;
        restrictRecord.remove(player);
        player.offer(Keys.MAX_HEALTH, 20.0D);
        player.offer(Keys.FOOD_LEVEL, 20);
        player.getOrCreate(PotionEffectData.class).ifPresent(effects -> {
            for (PotionEffect effect : effects.effects()) {
                if (effect.getType().equals(PotionEffectTypes.SLOWNESS) && effect.getDuration() == Short.MAX_VALUE) {
                    effects.remove(effect);
                    return;
                }
            }
        });
    }

    public static void SetPlayerRestrict(Player player) {
        restrictRecord.put(player, LocalDateTime.now());
        player.offer(Keys.MAX_HEALTH, 10.0D);
        player.offer(Keys.FOOD_LEVEL, 10);
        player.getOrCreate(PotionEffectData.class).ifPresent(effects -> {
            effects.addElement(Potions.DivineWrathPotion);
            player.offer(effects);
        });
    }
}
