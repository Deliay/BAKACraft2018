package com.mcbaka.bakacraft.Modules.Survival.MagicStoneHelper.Cast;

import com.mcbaka.bakacraft.Main;
import com.mcbaka.bakacraft.Modules.Survival.MagicStoneHelper.MagicStoneItems;
import com.mcbaka.bakacraft.Modules.Survival.MagicStoneHelper.PersistentKeys;
import com.mcbaka.bakacraft.Text.MagicStoneText;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.item.recipe.crafting.Ingredient;
import org.spongepowered.api.item.recipe.crafting.ShapelessCraftingRecipe;

public class TeleportReipes {

    public static Ingredient GetTypeIngredient(ItemStack base, TeleportType type) {
        Ingredient.Builder baseIngredient = Ingredient.builder().with(base);
        switch (type) {
            case HOME: return baseIngredient.with(
                    ItemTypes.LOG, ItemTypes.BONE, ItemTypes.GOLD_INGOT
            ).build();
            case MAIN_CITY: return baseIngredient.with(
                    ItemTypes.STICK, ItemTypes.BONE, ItemTypes.IRON_INGOT
            ).build();
            case FLAG: return baseIngredient.with(
                    ItemTypes.STICK, ItemTypes.BONE_BLOCK, ItemTypes.IRON_INGOT
            ).build();
            case ANY_POSITION: return baseIngredient.with(
                    ItemTypes.SAPLING, ItemTypes.BONE_BLOCK,
                    ItemTypes.GOLD_INGOT, ItemTypes.GOLD_INGOT, ItemTypes.GOLD_INGOT, ItemTypes.GOLD_INGOT
            ).build();
            case PLAYER_TELEPORT: return baseIngredient.with(
                    ItemTypes.BOOK
            ).with(CastBase.GetRegistry(Teleport.class).GenerateFromMagicStone(base, TeleportType.ANY_POSITION)).build();
        }
        return baseIngredient.with(ItemTypes.AIR).build();
    }

    public static ShapelessCraftingRecipe GetLocationRecipe(int n, TeleportType type) {
        ItemStack itemStack = MagicStoneItems.MagicStoneLvN(n);
        ShapelessCraftingRecipe.Builder builder = ShapelessCraftingRecipe.builder();

        return builder
                .addIngredient(GetTypeIngredient(itemStack, type))
                .result(CastBase.GetRegistry(Teleport.class).GenerateFromMagicStone(itemStack, type))
                .build(MagicStoneText.TeleportRecipeId(n, type), Main.GetInstance());
    }
}
