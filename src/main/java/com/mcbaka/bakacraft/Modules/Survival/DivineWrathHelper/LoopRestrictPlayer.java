package com.mcbaka.bakacraft.Modules.Survival.DivineWrathHelper;

import org.spongepowered.api.data.manipulator.mutable.PotionEffectData;
import org.spongepowered.api.effect.potion.PotionEffect;
import org.spongepowered.api.effect.potion.PotionEffectTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;

import java.util.Set;
import java.util.concurrent.TimeUnit;

public class LoopRestrictPlayer {
    public static Task.Builder TaskBuilder = Task
    .builder().interval(5, TimeUnit.SECONDS)
    .execute(() -> {
        // Check all restricted player
        Set<Player> players = PlayerDeadManager.GetAllRestrictedPlayer();
        for (Player player : players) {
            if (!PlayerDeadManager.PlayerIsInRestrict(player)) {
                PlayerDeadManager.RemovePlayerRestrict(player);
            }
            player.getOrCreate(PotionEffectData.class).ifPresent(effects -> {
                for (PotionEffect effect : effects.effects()) {
                    if (effect.getType().equals(PotionEffectTypes.SLOWNESS)) return;
                }
                effects.addElement(Potions.DivineWrathPotion);
            });
        }
    });
}
