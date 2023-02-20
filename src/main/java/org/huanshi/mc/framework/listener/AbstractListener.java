package org.huanshi.mc.framework.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.huanshi.mc.framework.AbstractPlugin;
import org.huanshi.mc.framework.engine.Component;
import org.huanshi.mc.framework.engine.Registrable;
import org.huanshi.mc.framework.annotation.Autowired;

public abstract class AbstractListener implements Component, Registrable, Listener {
    @Autowired
    private AbstractPlugin plugin;

    @Override
    public void load() {}

    @Override
    public void register() {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
}
