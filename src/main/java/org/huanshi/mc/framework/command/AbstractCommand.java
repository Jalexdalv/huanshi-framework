package org.huanshi.mc.framework.command;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.huanshi.mc.framework.AbstractPlugin;
import org.huanshi.mc.framework.annotation.HuanshiCommand;
import org.huanshi.mc.framework.api.BukkitAPI;
import org.huanshi.mc.framework.pojo.IComponent;
import org.huanshi.mc.framework.pojo.Registrar;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public abstract class AbstractCommand implements IComponent, Registrar, TabExecutor {
    @Getter
    private final @NotNull String name = StringUtils.trimToNull(getClass().getAnnotation(HuanshiCommand.class).name());
    private final PluginCommand pluginCommand = Objects.requireNonNull(BukkitAPI.getPluginCommand(name));

    @Override
    public void onCreate(@NotNull AbstractPlugin plugin) {}

    @Override
    public void onLoad(@NotNull AbstractPlugin plugin) {}

    @Override
    public void register(@NotNull AbstractPlugin plugin) {
        pluginCommand.setExecutor(this);
        pluginCommand.setTabCompleter(this);
    }

    public @NotNull String getLabel() {
        return pluginCommand.getLabel();
    }

    public @NotNull String getDescription() {
        return pluginCommand.getDescription();
    }

    public @NotNull String getUsage() {
        return pluginCommand.getUsage();
    }

    public @Nullable String getPermission() {
        return pluginCommand.getPermission();
    }

    public @NotNull List<String> getAliases() {
        return pluginCommand.getAliases();
    }
}
