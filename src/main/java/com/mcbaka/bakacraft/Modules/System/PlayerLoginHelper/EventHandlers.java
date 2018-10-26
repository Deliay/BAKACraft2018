package com.mcbaka.bakacraft.Modules.System.PlayerLoginHelper;

import com.mcbaka.bakacraft.Text.LoginText;
import com.mcbaka.bakacraft.Util.IEventHandler;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.command.SendCommandEvent;
import org.spongepowered.api.event.entity.DamageEntityEvent;
import org.spongepowered.api.event.entity.InteractEntityEvent;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.filter.Getter;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.event.item.inventory.*;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;

public class EventHandlers implements IEventHandler {

    @Listener(order = Order.FIRST, beforeModifications = true)
    public void onPlayerExecuteCommand(SendCommandEvent event, @First Player player) {
        if (LoginManager.PlayerIsPendingLogin(player)) {
            if (!event.getCommand().equals(LoginText.COMMAND_LOGIN.toPlain())) {
                player.sendMessage(LoginText.REQUIRE_LOGIN);
                event.setCancelled(true);
            }
        }
    }

    @Listener(order = Order.FIRST, beforeModifications = true)
    public void onPlayerJoinServer(ClientConnectionEvent.Join onJoin, @First Player player) {
        //走正常登录流程
        if (AuthenticationUtil.IsPlayerRegistered(player.getName())) {
            LoginManager.SetPlayerToPendingLogin(player);
            player.offer(Keys.GAME_MODE, GameModes.SPECTATOR);
            player.sendMessage(LoginText.LOGIN_TIPS);
            LoginManager.StorePlayerPosition(player);
        } else {
            player.kick(LoginText.NOT_REGISTERED);
        }

    }

    private void shouldCancelEvent(Cancellable event, Player player) {
        if (LoginManager.PlayerIsPendingLogin(player)) {
            event.setCancelled(true);
        }
    }

    @Listener(order = Order.FIRST, beforeModifications = true)
    public void onPlayerMove(MoveEntityEvent event, @First Player player) {
        shouldCancelEvent(event, player);
    }

    @Listener(order = Order.FIRST, beforeModifications = true)
    public void onChat(MessageChannelEvent.Chat chatEvent, @First Player player) {
        shouldCancelEvent(chatEvent, player);
    }
    @Listener(order = Order.FIRST, beforeModifications = true)
    public void onPlayerItemDrop(DropItemEvent dropItemEvent, @First Player player) {
        shouldCancelEvent(dropItemEvent, player);
    }

    @Listener(order = Order.FIRST, beforeModifications = true)
    public void onPlayerItemPickup(ChangeInventoryEvent.Pickup pickupItemEvent, @Root Player player) {
        shouldCancelEvent(pickupItemEvent, player);
    }

    @Listener(order = Order.FIRST, beforeModifications = true)
    public void onItemConsume(UseItemStackEvent.Start itemConsumeEvent, @First Player player) {
        shouldCancelEvent(itemConsumeEvent, player);
    }

    @Listener(order = Order.FIRST, beforeModifications = true)
    public void onItemInteract(InteractItemEvent interactItemEvent, @First Player player) {
        shouldCancelEvent(interactItemEvent, player);
    }

    @Listener(order = Order.FIRST, beforeModifications = true)
    public void onInventoryChange(ChangeInventoryEvent changeInventoryEvent, @First Player player) {
        shouldCancelEvent(changeInventoryEvent, player);
    }

    @Listener(order = Order.FIRST, beforeModifications = true)
    public void onInventoryInteract(InteractInventoryEvent interactInventoryEvent, @First Player player) {
        shouldCancelEvent(interactInventoryEvent, player);
    }

    @Listener(order = Order.FIRST, beforeModifications = true)
    public void onInventoryClick(ClickInventoryEvent clickInventoryEvent, @First Player player) {
        shouldCancelEvent(clickInventoryEvent, player);
    }

    @Listener(order = Order.FIRST, beforeModifications = true)
    public void onBlockInteract(InteractBlockEvent interactBlockEvent, @First Player player) {
        shouldCancelEvent(interactBlockEvent, player);
    }

    @Listener(order = Order.FIRST, beforeModifications = true)
    public void onPlayerInteractEntity(InteractEntityEvent interactEntityEvent, @First Player player) {
        shouldCancelEvent(interactEntityEvent, player);
    }

    @Listener(order = Order.FIRST, beforeModifications = true)
    public void onPlayerDamage(DamageEntityEvent damageEntityEvent, @First Player player) {
        //player is damage source
        shouldCancelEvent(damageEntityEvent, player);
    }

    @Listener(order = Order.FIRST, beforeModifications = true)
    public void onDamagePlayer(DamageEntityEvent damageEntityEvent, @Getter("getTargetEntity") Player player) {
        //player is damage target
        shouldCancelEvent(damageEntityEvent, player);
    }
}
