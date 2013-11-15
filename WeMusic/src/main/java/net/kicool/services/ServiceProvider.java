
package net.kicool.services;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

public class ServiceProvider {

    private static ServiceProvider instance;
    private final Context context;
    private Map<String, IService> serviceMap = new HashMap<String, IService>();

    public ServiceProvider(Context context) {
        this.context = context;

        registerServices();
    }

    private void registerServices() {
        IService services[] = {
                new BusService(),
                new WifiNetworkService(),
                new PeersMgr()};
        for (IService s : services) {
            s.init(this);
            register(s);
        }
    }

    public static ServiceProvider getInstance() {
        return instance;
    }

    public static void init(Context context) {
        instance = new ServiceProvider(context);
    }

    public Context getContext() {
        return context;
    }

    public void register(IService service) {
        serviceMap.put(service.getName(), service);
    }

    public IService getServiceInstance(String name) {
        return serviceMap.get(name);
    }
}
