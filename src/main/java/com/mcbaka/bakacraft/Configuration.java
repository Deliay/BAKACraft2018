package com.mcbaka.bakacraft;

import com.mcbaka.bakacraft.Logger.Logger;
import com.mcbaka.bakacraft.Util.IConfigurationHolder;
import com.mcbaka.bakacraft.Util.InjectConfig;
import com.mcbaka.bakacraft.Util.InjectConfigRoot;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.HashMap;

public class Configuration {
    private Path baseDirectory;
    private Configuration(Path directory) {
        this.baseDirectory = directory;
        ourInstance = this;
    }

    private HashMap<Class, HoconConfigurationLoader> configLoader = new HashMap<>();
    public HoconConfigurationLoader getConfigurationLoader(Class clazz) {
        return configLoader.get(clazz);
    }
    public <T extends IConfigurationHolder> HoconConfigurationLoader generateConfiguration(T instance) {
        String classSimpleName = instance.getClass().getSimpleName();
        Path confPath = baseDirectory.resolve(classSimpleName + ".conf");
        File confFile = confPath.toFile();
        boolean newCreate = false;
        if (!confFile.exists()) {
            try {
                if (confFile.createNewFile()) {
                    Logger.getInstance().getLogger().info("Create empty file for " + classSimpleName);
                    newCreate = true;
                } else {
                    Logger.getInstance().getLogger().info("Can't create default empty file for " + classSimpleName);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Logger.getInstance().getLogger().info("Load local configuration to " + classSimpleName);
        }
        HoconConfigurationLoader loader = HoconConfigurationLoader.builder().setFile(confFile).build();
        try {
            loader.load();
            if (newCreate) {
                Logger.getInstance().getLogger().info("Initialize default config of " + classSimpleName);
                ConfigurationNode defaultRoot = root(loader);
                instance.initializeConfig(defaultRoot);
                loader.save(defaultRoot);
            }
            configLoader.put(instance.getClass(), loader);
        } catch (IOException e) {
            Logger.getInstance().getLogger().error("Can't create instance of " + loader.toString());
        }
        for(Field field : instance.getClass().getDeclaredFields()) {
            if (field.getAnnotation(InjectConfig.class) != null) {
                try {
                    field.set(instance, loader);
                } catch (IllegalAccessException e) {
                    Logger.getInstance().getLogger().error("Can't inject to @InjectConfig annotation field");
                }
                break;
            } else if (field.getAnnotation(InjectConfigRoot.class) != null) {
                try {
                    field.set(instance, root(loader));
                } catch (IllegalAccessException e) {
                    Logger.getInstance().getLogger().error("Can't inject to @InjectConfigRoot annotation field");
                } catch (IOException e) {
                    Logger.getInstance().getLogger().error("Can't load root element form configuration file");
                }
            }
        }
        return loader;
    }

    private static Configuration ourInstance;
    static void CreateInstance(Path directory) {
        ourInstance = new Configuration(directory);
    }
    public static Configuration GetInstance() {
        return ourInstance;
    }
    public static ConfigurationNode root(ConfigurationLoader<CommentedConfigurationNode> holder) throws IOException {
        return holder.load(ConfigurationOptions.defaults());
    }
}
