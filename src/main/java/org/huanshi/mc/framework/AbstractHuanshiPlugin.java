package org.huanshi.mc.framework;

import lombok.SneakyThrows;
import net.kyori.adventure.text.Component;
import org.bukkit.plugin.java.JavaPlugin;
import org.huanshi.mc.framework.annotation.HuanshiAutowired;
import org.huanshi.mc.framework.api.BukkitAPI;
import org.huanshi.mc.framework.pojo.IHuanshiComponent;
import org.huanshi.mc.framework.lang.Zh;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractHuanshiPlugin extends JavaPlugin implements IHuanshiComponent {
    @HuanshiAutowired
    private Zh zh;
    private Component enableComponent, disableComponent;

    @Override
    public void onCreate(@NotNull AbstractHuanshiPlugin huanshiPlugin) {}

    @Override
    public void onLoad(@NotNull AbstractHuanshiPlugin huanshiPlugin) {
        enableComponent = zh.format(zh.getComponent("enable"), getName());
        disableComponent = zh.format(zh.getComponent("disable"), getName());
    }

    @SneakyThrows
    @Override
    public void onEnable() {
        BukkitAPI.createDataFolder(this);
        Bootstrap.scan(this);
        BukkitAPI.sendConsoleMessage(enableComponent);
    }

    @Override
    public void onDisable() {
        BukkitAPI.cancelAllTasks(this);
        BukkitAPI.sendConsoleMessage(disableComponent);
    }
}
