package com.mcbaka.bakacraft.Modules.Survival.MagicStoneHelper.Cast;

import com.mcbaka.bakacraft.Modules.Survival.MagicStoneHelper.Configurations;
import com.mcbaka.bakacraft.Modules.Survival.MagicStoneHelper.PersistentKeys;
import com.mcbaka.bakacraft.Modules.Survival.MagicStoneHelper.TeleportDelayService;
import com.mcbaka.bakacraft.Text.MagicStoneText;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

public class TeleportHelper {
    private static HashMap<UUID, LocalDateTime> TeleportTime = new HashMap<>();
    private static HashSet<UUID> StoneTeleport = new HashSet<>();

    /**
     * Call on player select any other player in teleport player text
     */
    public static void TeleportPlayerCallback(UUID stoneUUID, Player targetPlayer, Player fromPlayer, int delay) {
        if (StoneTeleport.contains(stoneUUID)) {
            if (TeleportTime.get(stoneUUID).plusSeconds(30).isBefore(LocalDateTime.now())) {
                fromPlayer.sendMessage(MagicStoneText.CastTeleportPending);
                return;
            }
            targetPlayer.sendMessage(MagicStoneText.CastTeleportToPlayer(stoneUUID, fromPlayer, targetPlayer, delay));
        }
    }

    public static void TeleportPlayerClean(UUID stoneUUID) {
        TeleportTime.remove(stoneUUID);
        StoneTeleport.remove(stoneUUID);
    }

    public static ItemStack AnyPlayer(ItemStackSnapshot item, Player player) {
        Optional<UUID> stoneUUID = item.get(PersistentKeys.STONE_UUID);
        int level = item.get(PersistentKeys.STONE_LEVEL).orElse(1);
        stoneUUID.ifPresent(uuid -> player.sendMessage(MagicStoneText.CastTeleportPlayerList(uuid, CastBaseHelper.StoneCastTime[level])));
        return CastBaseHelper.IncreaseUsage(item, player);
    }

    public static ItemStack TeleportMainCity(ItemStackSnapshot item, Player player) {
        int level = item.get(PersistentKeys.STONE_LEVEL).orElse(1);
        TeleportDelayService.TeleportPlayer(player, CastBaseHelper.StoneCastTime[level], GetWorldRespawnPosition());
        return CastBaseHelper.IncreaseUsage(item, player);
    }

    public static Location<World> GetWorldRespawnPosition() {
        Optional<World> world = Sponge.getServer().getWorld(Sponge.getServer().getDefaultWorldName());
        return world.map(World::getSpawnLocation).orElse(null);
    }

    public static ItemStack SetPlayerFlag(ItemStackSnapshot item, Player player) {
        Configurations.getInstance().setPlayerFlagPosition(player, player.getLocation());
        return CastBaseHelper.IncreaseUsage(item, player);
    }

    public static ItemStack AnyPosition(ItemStackSnapshot item, Player player) {
        // in any pos setting mode
        Optional<Location<World>> flag = Configurations.getInstance().getPlayerFlagPosition(player.getName());
        if (flag.isPresent()) {
            // clear mode
            ItemStack stack = item.createStack();
            stack.offer(PersistentKeys.TELEPORT_POS, flag.get());
            player.sendMessage(MagicStoneText.CastRecordTeleportPosition);
            return stack;
        } else {
            Optional<Location<World>> pos = item.get(PersistentKeys.TELEPORT_POS);
            if (pos.isPresent()) {
                int level = item.get(PersistentKeys.STONE_LEVEL).orElse(1);
                TeleportDelayService.TeleportPlayer(player, CastBaseHelper.StoneCastTime[level], pos.get());
                player.sendMessage(MagicStoneText.CastAlreadyTeleport);
                return CastBaseHelper.IncreaseUsage(item, player);
            } else {
                player.sendMessage(MagicStoneText.CastNotSetHome);
            }
        }
        return item.createStack();
    }

    public static ItemStack HomePlayer(ItemStackSnapshot item, Player player) {
        // in home setting mode
        Optional<Location<World>> flag = Configurations.getInstance().getPlayerFlagPosition(player.getName());
        if (flag.isPresent()) {
            // clear mode
            Configurations.getInstance().setPlayerFlagPosition(player, null);
            Configurations.getInstance().setPlayerHome(player.getName(), flag.get());
            player.sendMessage(MagicStoneText.CastRecordTeleportPosition);
        } else {
            // teleport mode
            Optional<Location<World>> home = Configurations.getInstance().getPlayerHome(player.getName());
            if (home.isPresent()) {
                int level = item.get(PersistentKeys.STONE_LEVEL).orElse(1);
                TeleportDelayService.TeleportPlayer(player, CastBaseHelper.StoneCastTime[level], home.get());
                player.sendMessage(MagicStoneText.CastAlreadyTeleport);
                return CastBaseHelper.IncreaseUsage(item, player);
            } else {
                player.sendMessage(MagicStoneText.CastNotSetHome);
            }
        }
        return item.createStack();
    }
}
