package org.huanshi.mc.framework.command;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.huanshi.mc.framework.engine.Component;
import org.huanshi.mc.framework.engine.Registrable;
import org.huanshi.mc.framework.lang.Zh;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public abstract sealed class AbstractCommand implements Component, Registrable, CommandExecutor permits AbstractConsoleCommand, AbstractPlayerCommand {
    protected String name;
    protected String[] args;

    @Override
    public void onCreate() {}

    @Override
    public void onLoad() {}

    @Override
    public void register() {
        Objects.requireNonNull(Bukkit.getPluginCommand(name)).setExecutor(this);
    }

    @Override
    public abstract boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String name, @NotNull String @NotNull [] args);

    protected @Nullable Player findPlayer(@NotNull Player player, @NotNull String targetPlayerName) {
        Player targetPlayer = Bukkit.getPlayerExact(targetPlayerName);
        if (targetPlayer == null) {
            player.sendMessage(Zh.PLAYER_NOT_FOUND);
        }
        return targetPlayer;
    }

    protected @Nullable World findWorld(@NotNull Player player, @NotNull String worldName) {
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            player.sendMessage(Zh.WORLD_NOT_FOUND);
        }
        return world;
    }

    public @NotNull String getName() {
        return name;
    }

    public @NotNull String[] getArgs() {
        return args;
    }
}
