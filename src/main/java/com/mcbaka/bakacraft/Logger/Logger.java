package com.mcbaka.bakacraft.Logger;

import com.mcbaka.bakacraft.Modules.AbstractModule;

public class Logger {
    private org.slf4j.Logger logger;
    private Logger(org.slf4j.Logger logger) {
        this.logger = logger;
    }

    public org.slf4j.Logger getLogger() {
        return logger;
    }

    private static Logger ourInstance;
    public static void CreateInstance(org.slf4j.Logger logger) {
        ourInstance = new Logger(logger);
    }

    public static Logger getInstance() {
        return ourInstance;
    }

    public static PrefixLogger DispatchNewModulesLogger(Class<? extends AbstractModule> moduleClass) {
        return new PrefixLogger(moduleClass.getSimpleName(), getInstance().logger);
    }
}
