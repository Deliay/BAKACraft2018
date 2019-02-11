package com.mcbaka.bakacraft.Modules.Survival;

import com.mcbaka.bakacraft.Modules.AbstractModule;
import com.mcbaka.bakacraft.Modules.Survival.MagicStoneHelper.Configurations;
import com.mcbaka.bakacraft.Modules.Survival.MagicStoneHelper.MagicStoneRecipes;
import com.mcbaka.bakacraft.Util.IConfigurationHolder;
import com.mcbaka.bakacraft.Util.InjectConfig;
import com.mcbaka.bakacraft.Util.InjectConfigRoot;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.Sponge;

public class MagicStone extends AbstractModule implements IConfigurationHolder {
    static {
        // load all recipes
        for (int i = 0; i < 7; i++) {
            Sponge.getRegistry().getCraftingRecipeRegistry().register(MagicStoneRecipes.GetMagicStoneLvNRecipe(i + 1));
        }
    }

    @InjectConfigRoot
    public ConfigurationNode config;

    @Override
    public void Load() {
        Configurations.CreateInstance(config.getNode("magic"));
    }

    @Override
    public void Unload() {

    }

    @Override
    public void initializeConfig(ConfigurationNode node) {
    }
}
