package com.mcbaka.bakacraft.Util;

import com.mcbaka.bakacraft.Configuration;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;

import java.io.IOException;

public interface IConfigurationHolder {
    void initializeConfig(ConfigurationNode node);

    default void SaveCofiguration() {
        HoconConfigurationLoader loader = Configuration.GetInstance().getConfigurationLoader(this.getClass());
        try {
            loader.save(Configuration.root(loader));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
