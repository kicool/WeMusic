package net.kicool.services;

/**
 * Created by kicoolzhang on 11/15/13.
 */
public interface IService {
    public void init(ServiceProvider provider);

    public void start();

    public void stop();

    public void restart();

    public String getName();
}
