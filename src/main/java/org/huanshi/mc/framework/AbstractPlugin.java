package org.huanshi.mc.framework;

import lombok.SneakyThrows;
import net.kyori.adventure.text.Component;
import org.bukkit.plugin.java.JavaPlugin;
import org.huanshi.mc.framework.annotation.Autowired;
import org.huanshi.mc.framework.api.BukkitAPI;
import org.huanshi.mc.framework.pojo.HuanshiComponent;
import org.huanshi.mc.framework.engine.Scanner;
import org.huanshi.mc.framework.lang.Zh;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractPlugin extends JavaPlugin implements HuanshiComponent {
    @Autowired
    private Zh zh;
    protected Component enable, disable;

    @Override
    public void create(@NotNull AbstractPlugin plugin) {}

    @Override
    public void load(@NotNull AbstractPlugin plugin) {
        enable = zh.formatComponent(zh.getComponent("enable"), getName());
        disable = zh.formatComponent(zh.getComponent("disable"), getName());
    }

    @SneakyThrows
    @Override
    public void onEnable() {
        BukkitAPI.createDataFolder(this);
        Scanner.scan(this);
        BukkitAPI.sendConsoleMessage(enable);
    }

    @Override
    public void onDisable() {
        BukkitAPI.cancelAllTasks(this);
        BukkitAPI.sendConsoleMessage(disable);
    }
}
