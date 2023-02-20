package org.huanshi.mc.framework;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.huanshi.mc.framework.engine.ComponentFactory;
import org.huanshi.mc.framework.lang.Zh;

import java.io.File;
import java.nio.file.Files;

public abstract class AbstractPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        try {
            File dataFolder = getDataFolder();
            if (!dataFolder.exists()) {
                Files.createDirectory(dataFolder.toPath());
            }
            ComponentFactory.create(this);
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
        start();
        Bukkit.getConsoleSender().sendMessage(Zh.enable(getName()));
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
        stop();
        Bukkit.getConsoleSender().sendMessage(Zh.disable(getName()));
    }

    protected void start() {}

    protected void stop() {}
}
