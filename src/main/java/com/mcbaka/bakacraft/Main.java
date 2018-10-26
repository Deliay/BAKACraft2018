package com.mcbaka.bakacraft;

import com.google.inject.Inject;
import com.mcbaka.bakacraft.Logger.Logger;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameConstructionEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;

@Plugin(id="bakacraft2018", name = "BAKACraft 2018", version = "1.0", description = "BAKACraft 2018 Plug-ins")
public class Main {
    @Inject
    @ConfigDir(sharedRoot = false)
    public Path defaultConfig;
    public static URL offsite = null;

    @Inject
    public org.slf4j.Logger logger;

    private static Main ourInstance;
    public static Main GetInstance() {
        return ourInstance;
    }

    @Listener
    public void onGameConstructor(GameConstructionEvent ev) {
        try {
            offsite = new URL("https://mcbaka.com");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        ourInstance = this;
        Logger.CreateInstance(logger);
    }

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        logger.info("BAKACraft 2018 in Sponge loaded.");
    }

    @Listener
    public void onGamePreInitializationEvent(GameInitializationEvent ev) {
        Configuration.CreateInstance(defaultConfig);
        ModulesLoader.CreateInstance();
    }
}
