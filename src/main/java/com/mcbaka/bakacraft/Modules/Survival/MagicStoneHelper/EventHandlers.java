package com.mcbaka.bakacraft.Modules.Survival.MagicStoneHelper;

import com.mcbaka.bakacraft.Modules.Survival.MagicStoneHelper.Cast.CastBaseHelper;
import com.mcbaka.bakacraft.Util.IEventHandler;
import com.mcbaka.bakacraft.Util.PersistentHelper;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.game.GameRegistryEvent;
import org.spongepowered.api.event.item.inventory.UseItemStackEvent;
import org.spongepowered.api.util.TypeTokens;

public class EventHandlers implements IEventHandler {
    @Listener
    public void onItemUse(UseItemStackEvent.Start event, @First Player player) {
        CastBaseHelper.OnItemUse(event, player);
    }

    @Listener
    public void onRegisterKey(GameRegistryEvent.Register<Key<?>> event) {
        PersistentKeys.CAST_COUNT = PersistentHelper.BuildPersistentKeysOfT(
                Key.builder().type(TypeTokens.INTEGER_VALUE_TOKEN),
                "cast_count", "CastCount"
        ).build();

        PersistentKeys.CAST_TYPE = PersistentHelper.BuildPersistentKeysOfT(
                Key.builder().type(TypeTokens.STRING_VALUE_TOKEN),
                "cast_type", "CastType"
        ).build();

        PersistentKeys.STONE_LEVEL = PersistentHelper.BuildPersistentKeysOfT(
                Key.builder().type(TypeTokens.INTEGER_VALUE_TOKEN),
                "stone_level", "StoneLevel"
        ).build();

        PersistentKeys.TELEPORT_TYPE = PersistentHelper.BuildPersistentKeysOfT(
                Key.builder().type(TypeTokens.INTEGER_VALUE_TOKEN),
                "teleport_type", "TeleportType"
        ).build();

        PersistentKeys.TELEPORT_POS = PersistentHelper.BuildPersistentKeysOfT(
                Key.builder().type(PersistentKeys.TELEPORT_POS_TOKEN),
                "teleport_axis", "TeleportAxis"
        ).build();

        PersistentKeys.STONE_UUID = PersistentHelper.BuildPersistentKeysOfT(
                Key.builder().type(TypeTokens.UUID_VALUE_TOKEN),
                "stone_uuid", "StoneUUID"
        ).build();
    }
}
