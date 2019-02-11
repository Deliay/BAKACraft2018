package com.mcbaka.bakacraft.Modules.Survival.MagicStoneHelper.Cast;

import com.mcbaka.bakacraft.Modules.Survival.MagicStoneHelper.PersistentKeys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;

public class Teleport extends CastBase {
    // class was hold by CastBase constructor
    static { new Teleport(); }
    private Teleport() {
        super("tale prat or al we go", "传送");
    }

    @Override
    public int getLevelCastCount(int level) {
        switch (level) {
            case 7: return 2000;
            case 1: return 20;
            default: return 2 * getLevelCastCount(level - 1);
        }
    }

    @Override
    public ItemStack OnItemUse(ItemStackSnapshot thisCast, Player player) {
        int type = thisCast.get(PersistentKeys.TELEPORT_TYPE).orElse(0);
        TeleportType teleportType = TeleportType.values()[type];
        switch (teleportType) {
            case MAIN_CITY: return TeleportHelper.TeleportMainCity(thisCast, player);
            case FLAG: return TeleportHelper.SetPlayerFlag(thisCast, player);
            case HOME: return TeleportHelper.HomePlayer(thisCast, player);
            case ANY_POSITION: return TeleportHelper.AnyPosition(thisCast, player);
            case PLAYER_TELEPORT: return TeleportHelper.AnyPlayer(thisCast, player);
            default: return thisCast.createStack();
        }
    }

    @Override
    public void RegisterRecipe() {

    }

    @Override
    protected ItemStack GenerateStone(ItemStack castBasedStone, Object data) {
        if (data instanceof TeleportType) {
            TeleportType type = (TeleportType) data;
            castBasedStone.offer(PersistentKeys.TELEPORT_TYPE, type.ordinal());
        }
        return castBasedStone;
    }
}
