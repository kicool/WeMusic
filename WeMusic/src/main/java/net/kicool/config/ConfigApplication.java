package net.kicool.config;

import net.kicool.common.config.IApplicationConfig;

/**
 * Created by kicoolzhang on 11/15/13.
 */
public enum ConfigApplication implements IApplicationConfig {
    INSTANCE;

    @Override
    public boolean DEBUG() {
        return true;
    }

    @Override
    public String PREFIX_DIR() {
        return "prefix";
    }

}
