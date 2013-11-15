package net.kicool.services;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * Created by kicoolzhang on 11/15/13.
 */
public class BusService extends BaseService {
    private Bus BUS;

    @Override
    public void init(ServiceProvider provider) {
        super.init(provider);

        BUS = new Bus(ThreadEnforcer.ANY);
    }

    public Bus getBus() {
        return BUS;
    }
}
