package org.huanshi.mc.framework.listener;

import org.bukkit.event.Listener;
import org.huanshi.mc.framework.AbstractPlugin;
import org.huanshi.mc.framework.api.BukkitAPI;
import org.huanshi.mc.framework.pojo.IComponent;
import org.huanshi.mc.framework.pojo.Registrar;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractListener implements IComponent, Registrar, Listener {
    @Override
    public void onCreate(@NotNull AbstractPlugin plugin) {}

    @Override
    public void onLoad(@NotNull AbstractPlugin plugin) {}

    @Override
    public void register(@NotNull AbstractPlugin plugin) {
        BukkitAPI.registerEvent(plugin, this);
    }
}
