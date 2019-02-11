package com.mcbaka.bakacraft.Modules.Survival.MagicStoneHelper.Cast;

import com.mcbaka.bakacraft.Modules.Survival.MagicStoneHelper.MagicStoneItems;
import com.mcbaka.bakacraft.Modules.Survival.MagicStoneHelper.PersistentKeys;
import com.mcbaka.bakacraft.Text.MagicStoneText;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.text.Text;

import java.util.HashMap;
import java.util.UUID;

public abstract class CastBase {
    private final static HashMap<Class, CastBase> CastClassMap = new HashMap<>();
    private final static HashMap<String, CastBase> CastMap = new HashMap<>();
    protected String CastId;
    protected String FriendlyName;
    protected CastBase(String id, String friendlyName) {
        RegisterCast(id, this);
        this.CastId = id;
        this.FriendlyName = friendlyName;
        this.RegisterRecipe();
    }

    public static <T extends CastBase> void RegisterCast(String id, T cast) {
        CastClassMap.put(cast.getClass(), cast);
        CastMap.put(id, cast);
    }

    public static <T extends CastBase> T GetRegistry(Class<T> clazz) {
        return (T)CastClassMap.get(clazz);
    }

    public static <T extends CastBase> T GetRegistry(String id) {
        return (T)CastMap.get(id);
    }

    public abstract int getLevelCastCount(int level);

    public Text GetLoreOfCurrentTypeCast() {
        return Text.of(MagicStoneText.CastTypePrefix, CastId);
    }

    /**
     * Duplicate a magic stone and given a new property with subclass of {@link CastBase}
     * @param magicStone magic stone
     * @return A magic stone subclass of {@link CastBase}
     */
    public ItemStack GenerateFromMagicStone(ItemStack magicStone, Object data)
    {
        ItemStack copy = magicStone.copy();
        copy.offer(PersistentKeys.CAST_TYPE, this.CastId);
        copy.offer(PersistentKeys.CAST_COUNT, 0);
        magicStone.offer(PersistentKeys.STONE_UUID, UUID.randomUUID());
        return GenerateStone(copy, data);
    }
    public ItemStack RestoreToMagicStone(ItemStack thisCast)
    {
        UUID id = thisCast.get(PersistentKeys.STONE_UUID).orElse(null);
        if (id == null) { return null; }
        return MagicStoneItems.MagicStoneLvN(thisCast.get(PersistentKeys.STONE_LEVEL).orElse(1));
    }
    public abstract ItemStack OnItemUse(ItemStackSnapshot thisCast, Player player);
    public abstract void RegisterRecipe();
    protected abstract ItemStack GenerateStone(ItemStack castBasedStone, Object arg);
}
