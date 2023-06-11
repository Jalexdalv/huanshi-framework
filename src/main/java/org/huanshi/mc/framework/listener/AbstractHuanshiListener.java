package org.huanshi.mc.framework.listener;

import org.bukkit.event.Listener;
import org.huanshi.mc.framework.AbstractHuanshiPlugin;
import org.huanshi.mc.framework.api.BukkitAPI;
import org.huanshi.mc.framework.pojo.IHuanshiComponent;
import org.huanshi.mc.framework.pojo.IHuanshiRegistrar;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractHuanshiListener implements IHuanshiComponent, IHuanshiRegistrar, Listener {
    @Override
    public void onCreate(@NotNull AbstractHuanshiPlugin huanshiPlugin) {}

    @Override
    public void onLoad(@NotNull AbstractHuanshiPlugin huanshiPlugin) {}

    @Override
    public void register(@NotNull AbstractHuanshiPlugin huanshiPlugin) {
        BukkitAPI.registerEvent(huanshiPlugin, this);
    }
}
