package org.huanshi.mc.framework;

import lombok.SneakyThrows;
import net.kyori.adventure.text.Component;
import org.bukkit.plugin.java.JavaPlugin;
import org.huanshi.mc.framework.annotation.Autowired;
import org.huanshi.mc.framework.api.BukkitAPI;
import org.huanshi.mc.framework.pojo.IComponent;
import org.huanshi.mc.framework.lang.Zh;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractPlugin extends JavaPlugin implements IComponent {
    @Autowired
    private Zh zh;
    private Component enable, disable;

    @Override
    public void onCreate(@NotNull AbstractPlugin plugin) {}

    @Override
    public void onLoad(@NotNull AbstractPlugin plugin) {
        enable = zh.format(zh.getComponent("enable"), getName());
        disable = zh.format(zh.getComponent("disable"), getName());
    }

    @SneakyThrows
    @Override
    public void onEnable() {
        BukkitAPI.createDataFolder(this);
        Bootstrap.scan(this);
        BukkitAPI.sendConsoleMessage(enable);
    }

    @Override
    public void onDisable() {
        BukkitAPI.cancelAllTasks(this);
        BukkitAPI.sendConsoleMessage(disable);
    }
}
