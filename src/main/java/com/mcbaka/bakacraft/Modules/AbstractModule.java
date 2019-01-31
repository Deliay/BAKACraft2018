package com.mcbaka.bakacraft.Modules;

import com.google.inject.Inject;
import com.mcbaka.bakacraft.Configuration;
import com.mcbaka.bakacraft.Logger.PrefixLogger;
import com.mcbaka.bakacraft.Main;
import com.mcbaka.bakacraft.ModulesLoader;
import com.mcbaka.bakacraft.Util.Holder;
import com.mcbaka.bakacraft.Util.IConfigurationHolder;
import com.mcbaka.bakacraft.Util.IEventHandler;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.scheduler.Task;

public abstract class AbstractModule {
    @Inject
    public PrefixLogger logger;

    public abstract void Load();
    public abstract void Unload();
    public static void RegisterListener(IEventHandler handler) {
        Sponge.getEventManager().registerListeners(Main.GetInstance(), handler);
    }
    public static void RegisterConfigrationHolder(IConfigurationHolder holder) {
        Configuration.GetInstance().generateConfiguration(holder);
    }
    public <T extends AbstractModule> T ModuleOf(Class<T> clazz) {
        return ModulesLoader.GetModule(clazz).target();
    }
    public static Task RegisterTaskBuilder(Task.Builder builder) {
        return builder.submit(Main.GetInstance());
    }
}
