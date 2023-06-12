package org.huanshi.mc.framework.command;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.huanshi.mc.framework.plugin.AbstractPlugin;
import org.huanshi.mc.framework.annotation.Command;
import org.huanshi.mc.framework.pojo.IComponent;
import org.huanshi.mc.framework.pojo.IRegistrar;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public abstract class AbstractCommand implements IComponent, IRegistrar, TabExecutor {
    @Getter
    private final String name = Objects.requireNonNull(StringUtils.trimToNull(getClass().getAnnotation(Command.class).name()));
    private final PluginCommand pluginCommand = Objects.requireNonNull(Bukkit.getPluginCommand(name));

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
