package com.mcbaka.bakacraft.Modules.Survival.DivineWrathHelper;

import com.mcbaka.bakacraft.Util.IEventHandler;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.cause.entity.damage.DamageModifier;
import org.spongepowered.api.event.entity.DamageEntityEvent;
import org.spongepowered.api.event.filter.Getter;
import org.spongepowered.api.event.filter.cause.First;

public class EventHandlers implements IEventHandler {
    @Listener(order = Order.LAST)
    public void onPlayerDamageOther(DamageEntityEvent event, @First Player player) {
        // if this player was restricted, reduce 50% percent damage
        if (PlayerDeadManager.PlayerIsInRestrict(player)) {
            event.setDamage(DamageModifier.builder().build(), (dmg) -> dmg  /2);
        }
    }

    @Listener(order = Order.LAST)
    public  void onPlayerDamageByOther(DamageEntityEvent event, @Getter("getTargetEntity") Player player) {
        // if target player was restricted, get 50% more damage
        if (PlayerDeadManager.PlayerIsInRestrict(player)) {
            event.setDamage(DamageModifier.builder().build(), (dmg) -> dmg * 2);
        }
    }
}
