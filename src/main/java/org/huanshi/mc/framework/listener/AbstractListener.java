package org.huanshi.mc.framework.listener;

import org.bukkit.event.Listener;
import org.huanshi.mc.framework.AbstractPlugin;
import org.huanshi.mc.framework.api.BukkitAPI;
import org.huanshi.mc.framework.pojo.HuanshiComponent;
import org.huanshi.mc.framework.pojo.Registrable;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractListener implements HuanshiComponent, Registrable, Listener {
    @Override
    public void create(@NotNull AbstractPlugin plugin) {}

    @Override
    public void load(@NotNull AbstractPlugin plugin) {}

    @Override
    public void register(@NotNull AbstractPlugin plugin) {
        BukkitAPI.registerEvent(plugin, this);
    }
}
