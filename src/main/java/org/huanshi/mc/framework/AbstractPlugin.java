package org.huanshi.mc.framework;

import org.bukkit.plugin.java.JavaPlugin;
import org.huanshi.mc.framework.api.BukkitApi;
import org.huanshi.mc.framework.engine.ComponentFactory;
import org.huanshi.mc.framework.lang.Zh;

public abstract class AbstractPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        try {
            BukkitApi.createDataFolder(this);
            ComponentFactory.scan(this);
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
        BukkitApi.sendConsoleMessage(Zh.enable(getName()));
    }

    @Override
    public void onDisable() {
        BukkitApi.cancelAllTasks(this);
        BukkitApi.sendConsoleMessage(Zh.disable(getName()));
    }
}
