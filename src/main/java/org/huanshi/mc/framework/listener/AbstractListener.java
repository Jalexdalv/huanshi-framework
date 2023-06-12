package org.huanshi.mc.framework.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.huanshi.mc.framework.plugin.AbstractPlugin;
import org.huanshi.mc.framework.pojo.IComponent;
import org.huanshi.mc.framework.pojo.IRegistrar;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractListener implements IComponent, IRegistrar, Listener {
    @Override
    public void onCreate(@NotNull AbstractPlugin plugin) {}

    @Override
    public void onLoad(@NotNull AbstractPlugin plugin) {}

    @Override
    public void register(@NotNull AbstractPlugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
}
