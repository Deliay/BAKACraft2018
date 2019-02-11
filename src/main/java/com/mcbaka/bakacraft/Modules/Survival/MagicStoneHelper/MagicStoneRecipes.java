package com.mcbaka.bakacraft.Modules.Survival.MagicStoneHelper;

import com.mcbaka.bakacraft.Main;
import com.mcbaka.bakacraft.Text.MagicStoneText;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.recipe.crafting.Ingredient;
import org.spongepowered.api.item.recipe.crafting.ShapelessCraftingRecipe;

public class MagicStoneRecipes {
    public static Ingredient GetMagicStoneLvNIngredient(int n) {
        Ingredient.Builder IngredientBuilder = Ingredient.builder();
        switch (n) {
            case 7:
                return IngredientBuilder
                        .with(MagicStoneItems.MagicStoneLvN(6))
                        .with(ItemTypes.BEDROCK)
                        .build();
            case 1:
                return IngredientBuilder
                        .with(ItemTypes.STONE, ItemTypes.BONE_BLOCK, ItemTypes.GLASS)
                        .build();
            case 2:
                return IngredientBuilder
                        .with(MagicStoneItems.MagicStoneLvN(1))
                        .with(ItemTypes.RED_FLOWER, ItemTypes.GLASS).build();
            case 6:
                return IngredientBuilder
                        .with(MagicStoneItems.MagicStoneLvN(5))
                        .with(ItemTypes.EMERALD, ItemTypes.EMERALD)
                        .with(ItemTypes.BLAZE_POWDER)
                        .build();
            case 3:
                return IngredientBuilder
                        .with(MagicStoneItems.MagicStoneLvN(2))
                        .with(ItemTypes.FLINT)
                        .with(ItemTypes.EMERALD)
                        .build();
            case 4:
                return IngredientBuilder
                    .with(MagicStoneItems.MagicStoneLvN(3))
                    .with(ItemTypes.FLINT)
                    .with(ItemTypes.EMERALD)
                    .build();
            case 5:
                return IngredientBuilder
                    .with(MagicStoneItems.MagicStoneLvN(4))
                    .with(ItemTypes.FLINT)
                    .with(ItemTypes.EMERALD)
                    .build();
            default:
                return Ingredient.NONE;
        }
    }
    @NotNull
    public static ShapelessCraftingRecipe GetMagicStoneLvNRecipe(int n) {
        return ShapelessCraftingRecipe.builder()
                .addIngredient(
                        Ingredient.builder()
                                .with(GetMagicStoneLvNIngredient(n))
                                .build()
                )
                .result(MagicStoneItems.MagicStoneLvN(n))
                .build(MagicStoneText.MagicStoneLvnRecipeId(n), Main.GetInstance());
    }
}
