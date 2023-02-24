package org.huanshi.mc.framework.listener;

import org.bukkit.event.Listener;
import org.huanshi.mc.framework.HuanshiPlugin;
import org.huanshi.mc.framework.api.BukkitAPI;
import org.huanshi.mc.framework.pojo.HuanshiComponent;
import org.huanshi.mc.framework.pojo.Registrable;
import org.jetbrains.annotations.NotNull;

public abstract class HuanshiListener implements HuanshiComponent, Registrable, Listener {
    @Override
    public void onCreate(@NotNull HuanshiPlugin huanshiPlugin) {}

    @Override
    public void onLoad(@NotNull HuanshiPlugin huanshiPlugin) {}

    @Override
    public void register(@NotNull HuanshiPlugin huanshiPlugin) {
        BukkitAPI.registerEvent(huanshiPlugin, this);
    }
}
