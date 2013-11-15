package net.kicool.config;

import net.kicool.common.config.IProductConfig;

/**
 * Created by kicoolzhang on 11/15/13.
 */
public class ConfigProduct implements IProductConfig {

    @Override
    public String getProductFlavorName() {
        return "baby";
    }

    @Override
    public String getDefaultLanguage() {
        return "zh-CN";
    }
}
