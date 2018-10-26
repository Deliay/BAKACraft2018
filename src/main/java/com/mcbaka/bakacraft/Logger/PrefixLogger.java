package com.mcbaka.bakacraft.Logger;

import org.slf4j.Logger;

public class PrefixLogger {
    private String prefix;
    private Logger logger;
    public PrefixLogger(String prefix, Logger logger) {
        this.prefix = prefix;
        this.logger = logger;
    }

    private String withPrefix(String msg) {
        return String.format("[%s]%s", prefix, msg);
    }

    public void info(String msg) {
        logger.info(withPrefix(msg));
    }

    public void warn(String msg) {
        logger.info(withPrefix(msg));
    }

    public void error(String msg) {
        logger.error(withPrefix(msg));
    }


}
