package org.huanshi.mc.framework.plugin;

import lombok.SneakyThrows;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.huanshi.mc.framework.Bootstrap;
import org.huanshi.mc.framework.annotation.Autowired;
import org.huanshi.mc.framework.lang.ZhLang;
import org.huanshi.mc.framework.pojo.IComponent;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Files;
import java.nio.file.Path;

public abstract class AbstractPlugin extends JavaPlugin implements IComponent {
    @Autowired
    private ZhLang zhLang;
    private Component enableComponent, disableComponent;

    @Override
    public void onCreate(@NotNull AbstractPlugin plugin) {}

    @Override
    public void onLoad(@NotNull AbstractPlugin plugin) {
        enableComponent = zhLang.format(zhLang.getComponent("enable"), getName());
        disableComponent = zhLang.format(zhLang.getComponent("disable"), getName());
    }

    @SuppressWarnings("all")
    @SneakyThrows
    @Override
    public void onEnable() {
        Path path = getDataFolder().toPath();
        if (!Files.exists(path)) {
            Files.createDirectory(path);
        }
        Bootstrap.scan(this);
        Bukkit.getConsoleSender().sendMessage(enableComponent);
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
        Bukkit.getConsoleSender().sendMessage(disableComponent);
    }
}
