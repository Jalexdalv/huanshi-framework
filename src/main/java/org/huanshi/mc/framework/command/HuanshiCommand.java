package org.huanshi.mc.framework.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract non-sealed class HuanshiCommand extends AbstractCommand {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String name, @NotNull String[] args) {
        return onCommand(commandSender, command.getPermission(), args);
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String name, @NotNull String[] args) {
        return onTabComplete(commandSender, command.getPermission(), args);
    }

    public abstract boolean onCommand(@NotNull CommandSender commandSender, @Nullable String permission, @NotNull String[] args);

    public abstract @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @Nullable String permission, @NotNull String[] args);

    public boolean hasPermission(@NotNull Player player) {
        return true;
    }
}
