package net.kicool.services;

import net.kicool.common.utils.LoggerUtil;

/**
 * Created by kicoolzhang on 11/15/13.
 */
abstract class BaseService implements IService {
    @Override
    public void init(ServiceProvider provider) {
        LoggerUtil.v(getName(), "service init");
    }

    @Override
    public void start() {
        LoggerUtil.v(getName(), "service start");
    }

    @Override
    public void stop() {
        LoggerUtil.v(getName(), "service stop");
    }

    @Override
    public void restart() {
        LoggerUtil.v(getName(), "service restart");
    }

    @Override
    public String getName() {
        return getClass().getSimpleName();
    }
}
