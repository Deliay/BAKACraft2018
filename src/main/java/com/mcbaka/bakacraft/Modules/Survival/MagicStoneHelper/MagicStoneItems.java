package com.mcbaka.bakacraft.Modules.Survival.MagicStoneHelper;

import com.mcbaka.bakacraft.Modules.Survival.MagicStoneHelper.Cast.CastBaseHelper;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.item.EnchantmentData;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.enchantment.Enchantment;
import org.spongepowered.api.item.enchantment.EnchantmentTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;

import java.util.UUID;

/**
 * Magic Stone {@link ItemStack} Helpers
 */
public class MagicStoneItems {
    /**
     * Get a color from current stone level
     * @param n Level
     * @return Color of current level
     */
    public static TextColor MagicStoneHintColor(int n) {
        if (n < 3) return TextColors.GREEN;
        else if (n < 5) return TextColors.BLUE;
        else if (n < 6) return TextColors.RED;
        else if (n < 7) return TextColors.GOLD;
        else if (n < 8) return TextColors.DARK_AQUA;
        else return TextColors.LIGHT_PURPLE;
    }
    /**
     * Generate a {@link ItemStack} as a MagicStone.
     * @param n N level
     * @return A {@link ItemStack} instance
     */
    public static ItemStack MagicStoneLvN(int n) {
        ItemStack magicStone = ItemStack.builder()
                .itemType(ItemTypes.PAPER)
                .build();
        magicStone.getOrCreate(EnchantmentData.class).ifPresent(
                enhanceData -> enhanceData.set(
                        enhanceData.enchantments().add(Enchantment.of(EnchantmentTypes.INFINITY, n))));
        magicStone.offer(Keys.DISPLAY_NAME, Text.of(
                MagicStoneHintColor(n), "等级", n,
                TextColors.AQUA, "卷轴"
        ));
        magicStone.offer(Keys.UNBREAKABLE, true);
        magicStone.offer(PersistentKeys.STONE_LEVEL, n);
        return magicStone;
    }

//    public final static ItemStack MagicStoneLv1 = CreateMagicStoneLvN(1);
//    public final static ItemStack MagicStoneLv2 = CreateMagicStoneLvN(2);
//    public final static ItemStack MagicStoneLv3 = CreateMagicStoneLvN(3);
//    public final static ItemStack MagicStoneLv4 = CreateMagicStoneLvN(4);
//    public final static ItemStack MagicStoneLv5 = CreateMagicStoneLvN(5);
//    public final static ItemStack MagicStoneLv6 = CreateMagicStoneLvN(6);
//    public final static ItemStack MagicStoneLv7 = CreateMagicStoneLvN(7);
//
//    public final static ItemStack[] MagicStone = new ItemStack[] {
//            MagicStoneLv1, MagicStoneLv1,
//            MagicStoneLv2, MagicStoneLv3, MagicStoneLv4,
//            MagicStoneLv5, MagicStoneLv6, MagicStoneLv7 };
//
//    public static ItemStack MagicStoneLvN(int n) {
//        return MagicStone[n];
//    }
}
