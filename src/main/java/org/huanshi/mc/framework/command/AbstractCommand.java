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
    public abstract boolean onCommand(@NotNull final CommandSender commandSender, @NotNull final Command command, @NotNull final String head, @NotNull final String @NotNull [] args);

    protected final @Nullable Player findPlayer(@NotNull final Player player, @NotNull final String targetPlayerName) {
        final Player targetPlayer = Bukkit.getPlayerExact(targetPlayerName);
        if (targetPlayer == null) {
            player.sendMessage(Zh.PLAYER_NOT_FOUND);
        }
        return targetPlayer;
    }

    protected final @Nullable World findWorld(@NotNull final Player player, @NotNull final String worldName) {
        final World world = Bukkit.getWorld(worldName);
        if (world == null) {
            player.sendMessage(Zh.WORLD_NOT_FOUND);
        }
        return world;
    }
}
