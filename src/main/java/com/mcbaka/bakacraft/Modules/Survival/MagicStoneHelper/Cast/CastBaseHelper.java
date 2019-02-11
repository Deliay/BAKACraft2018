package com.mcbaka.bakacraft.Modules.Survival.MagicStoneHelper.Cast;

import com.mcbaka.bakacraft.Modules.Survival.MagicStoneHelper.MagicStoneItems;
import com.mcbaka.bakacraft.Modules.Survival.MagicStoneHelper.PersistentKeys;
import com.mcbaka.bakacraft.Text.MagicStoneText;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.item.inventory.UseItemStackEvent;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class CastBaseHelper {
    public static int[] StoneCastTime = new int[] { 4, 4, 3, 2, 1, 1, 0, 0};
    public static int[] StoneCD = new int[] { 10, 10, 7, 5, 3, 1, 1, 0};
    public static int[] StoneFoodLevel = new int[] { 1, 1, 1, 1, 2, 2, 2 };

    public static List<Text> GetLore(@NotNull ItemStack castItem) {
        String id = castItem.get(PersistentKeys.CAST_TYPE).orElse(null);
        if (id == null) return null;
        int castCount = castItem.get(PersistentKeys.CAST_COUNT).orElse(0);
        int level = castItem.get(PersistentKeys.CAST_COUNT).orElse(1);
        String name = CastBase.GetRegistry(id).FriendlyName;
        String cast = CastBase.GetRegistry(id).CastId;
        return Arrays.asList(
                Text.of(MagicStoneText.CurrentLevelPrefix, level),
                Text.of(MagicStoneText.CastTypePrefix, cast),
                Text.of(MagicStoneText.CastFriendlyNamePrefix, name),
                Text.of(MagicStoneText.CastCountPrefix, castCount)
        );
    }

    public static Text GetTitle(@NotNull ItemStack castItem) {
        String id = castItem.get(PersistentKeys.CAST_TYPE).orElse(null);
        if (id == null) return null;
        int level = castItem.get(PersistentKeys.CAST_COUNT).orElse(1);
        String name = CastBase.GetRegistry(id).FriendlyName;
        return Text.of(
                MagicStoneItems.MagicStoneHintColor(level), "等级", level,
                TextColors.AQUA, name, " 卷轴"
        );
    }

    public static void OnItemUse(@NotNull UseItemStackEvent.Start event, @NotNull Player player) {
        String castType = event.getItemStackInUse().get(PersistentKeys.CAST_TYPE).orElse(null);
        if (castType == null) return;
        int level = event.getItemStackInUse().get(PersistentKeys.STONE_LEVEL).orElse(1);
        int castCount = event.getItemStackInUse().get(PersistentKeys.CAST_COUNT).orElse(9999);
        CastBase cast = CastBase.GetRegistry(castType);

        // cast count limit
        if (castCount >= cast.getLevelCastCount(level)) {
            player.sendMessage(MagicStoneText.CastNoMana);
            return;
        }

        // food level limit
        if (player.foodLevel().get() < StoneFoodLevel[level]) {
            player.sendMessage(MagicStoneText.CastFoodLevelNE);
            return;
        }

        Optional<UUID> castUUID = event.getItemStackInUse().get(PersistentKeys.STONE_UUID);
        // check cd limit
        if (castUUID.isPresent() && GetStoneIsCD(castUUID.get())) {
            player.sendMessage(MagicStoneText.CastTeleportPending);
            return;
        }
        cast.OnItemUse(event.getItemStackInUse(), player);
    }

    public static void IncreaseUsage(ItemStack item) {
        Optional<Integer> castCount = item.get(PersistentKeys.CAST_COUNT);
        if (castCount.isPresent()) {
            item.offer(PersistentKeys.CAST_COUNT, castCount.get() + 1);
        } else {
            item.offer(PersistentKeys.CAST_COUNT, 1);
        }
    }

    private static HashMap<UUID, LocalDateTime> StoneCDMap = new HashMap<>();
    public static void SetStoneInCD(UUID uuid, long delay) {
        StoneCDMap.put(uuid, LocalDateTime.now().plusSeconds(delay));
    }
    public static void RemoveStoneCD(UUID uuid) {
        StoneCDMap.remove(uuid);
    }
    public static boolean GetStoneIsCD(UUID uuid) {
        // exist and before now mean in CD
        return StoneCDMap.containsKey(uuid) && StoneCDMap.get(uuid).isBefore(LocalDateTime.now());
    }
    public static ItemStack IncreaseUsage(ItemStackSnapshot item, Player player) {
        Optional<Integer> castCount = item.get(PersistentKeys.CAST_COUNT);
        Optional<Integer> castLevel = item.get(PersistentKeys.STONE_LEVEL);
        Optional<UUID> castUUID = item.get(PersistentKeys.STONE_UUID);
        ItemStack stack = item.createStack();
        // increase cast count
        if (castCount.isPresent()) {
            stack.offer(PersistentKeys.CAST_COUNT, castCount.get() + 1);
        } else {
            stack.offer(PersistentKeys.CAST_COUNT, 1);
        }
        // consume food level and set CD
        if (castLevel.isPresent() && castUUID.isPresent()) {
            SetStoneInCD(castUUID.get(), StoneCD[castLevel.get()]);
            player.offer(Keys.FOOD_LEVEL, player.foodLevel().get() - StoneFoodLevel[castLevel.get()]);
        }
        return stack;
    }
}
