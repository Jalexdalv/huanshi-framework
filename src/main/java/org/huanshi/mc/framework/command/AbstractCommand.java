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

public abstract sealed class AbstractCommand implements Component, Registrable, CommandExecutor permits AbstractConsoleCommand, AbstractPlayerCommand {
    @Override
    public abstract boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String head, @NotNull String @NotNull [] args);

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
}
