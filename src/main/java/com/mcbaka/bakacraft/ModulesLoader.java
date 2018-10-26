package com.mcbaka.bakacraft;

import com.mcbaka.bakacraft.Logger.Logger;
import com.mcbaka.bakacraft.Modules.AbstractModule;
import com.mcbaka.bakacraft.Modules.Survival.DivineWrath;
import com.mcbaka.bakacraft.Modules.Util.MySQL;
import com.mcbaka.bakacraft.Modules.System.PlayerLogin;
import com.mcbaka.bakacraft.Modules.Survival.Scavenger;
import com.mcbaka.bakacraft.Modules.Survival.MagicStone;
import com.mcbaka.bakacraft.Util.Holder;
import com.mcbaka.bakacraft.Util.IConfigurationHolder;
import com.mcbaka.bakacraft.Util.IEventHandler;
import org.spongepowered.api.Sponge;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;

public class ModulesLoader {

    private HashMap<Class<? extends AbstractModule>, Holder<? extends AbstractModule>> modules;
    private ModulesLoader() {
        modules = new HashMap<>();
    }

    public <T extends AbstractModule> void RegisterModule(T module) {
        Logger.getInstance().getLogger().info("Loading: " + module.getClass().getSimpleName());
        tryInitialLogger(module);
        if (module instanceof IConfigurationHolder) {
            Configuration.GetInstance().generateConfiguration((IConfigurationHolder)module);
        }
        module.Load();
        if (module instanceof IEventHandler) {
            Sponge.getEventManager().registerListeners(Main.GetInstance(), module);
        }
        modules.put(module.getClass(), new Holder(module));
    }

    private <T extends AbstractModule> void tryInitialLogger(T instance) {
        try {
            Field field = AbstractModule.class.getField("logger");
            field.set(instance, Logger.DispatchNewModulesLogger(instance.getClass()));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            Logger.getInstance().getLogger().error("Can't initial of " + instance.getClass().getSimpleName());
            e.printStackTrace();
        }
    }

    public static <T extends AbstractModule> Holder<T> GetModule(Class<T> clazz) {
        return (Holder<T>) GetInstance().modules.getOrDefault(clazz, null);
    }

    public Collection<Holder<? extends AbstractModule>> AllModules() {
        return modules.values();
    }

    private static void LoadModules() {
        AbstractModule[] moduleList = new AbstractModule[] {
                new MySQL(),            // first order
                new PlayerLogin(),
                new Scavenger(),
                new MagicStone(),
                new DivineWrath(),
        };
        ModulesLoader loader = GetInstance();
        for (AbstractModule module : moduleList) {
            loader.RegisterModule(module);
        }
    }

    private static ModulesLoader ourInstance;
    public static void CreateInstance() {
        if (ourInstance != null) {
            Collection<Holder<? extends AbstractModule>> modules = ourInstance.AllModules();
            for (Holder<? extends AbstractModule> module : modules) {
                module.target().Unload();
            }
        }
        ourInstance = new ModulesLoader();
        LoadModules();
    }
    public static ModulesLoader GetInstance() {
        return ourInstance;
    }
}
