package org.huanshi.mc.framework.api;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitTask;
import org.huanshi.mc.framework.AbstractPlugin;
import org.huanshi.mc.framework.lang.Zh;
import org.huanshi.mc.framework.utils.FormatUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

public class BukkitApi {
    public static void registerCommandExecutor(@NotNull String name, @NotNull CommandExecutor commandExecutor) {
        Objects.requireNonNull(Bukkit.getPluginCommand(name)).setExecutor(commandExecutor);
    }

    public static void registerTabExecutor(@NotNull String name, @NotNull TabExecutor tabExecutor) {
        PluginCommand pluginCommand = Objects.requireNonNull(Bukkit.getPluginCommand(name));
        pluginCommand.setExecutor(tabExecutor);
        pluginCommand.setTabCompleter(tabExecutor);
    }

    public static @Nullable Player findPlayer(@NotNull Player player, @NotNull String targetPlayerName) {
        Player targetPlayer = Bukkit.getPlayerExact(targetPlayerName);
        if (targetPlayer == null) {
            player.sendMessage(Zh.PLAYER_NOT_FOUND);
        }
        return targetPlayer;
    }

    public static @Nullable World findWorld(@NotNull Player player, @NotNull String worldName) {
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            player.sendMessage(Zh.WORLD_NOT_FOUND);
        }
        return world;
    }

    public static void registerEvent(@NotNull AbstractPlugin plugin, @NotNull Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, plugin);
    }

    public static void createDataFolder(@NotNull AbstractPlugin plugin) throws IOException {
        File dataFolder = plugin.getDataFolder();
        if (!dataFolder.exists()) {
            Files.createDirectory(dataFolder.toPath());
        }
    }

    public static void sendConsoleMessage(@NotNull Component component) {
        Bukkit.getConsoleSender().sendMessage(component);
    }

    public static void cancelAllTask(@NotNull AbstractPlugin plugin) {
        Bukkit.getScheduler().cancelTasks(plugin);
    }

    public static void callEvent(@NotNull Event event) {
        Bukkit.getPluginManager().callEvent(event);
    }

    public static @NotNull BukkitTask runTask(@NotNull AbstractPlugin plugin, @NotNull Runnable runnable) {
        return Bukkit.getScheduler().runTask(plugin, runnable);
    }

    public static @NotNull BukkitTask runTaskAsynchronously(@NotNull AbstractPlugin plugin, @NotNull Runnable runnable) {
        return Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
    }

    public static @NotNull BukkitTask runTaskLater(@NotNull AbstractPlugin plugin, @NotNull Runnable runnable, long delay) {
        return Bukkit.getScheduler().runTaskLater(plugin, runnable, FormatUtils.convertDurationToTick(delay));
    }

    public static @NotNull BukkitTask runTaskLaterAsynchronously(@NotNull AbstractPlugin plugin, @NotNull Runnable runnable, long delay) {
        return Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, runnable, FormatUtils.convertDurationToTick(delay));
    }

    public static @NotNull BukkitTask runTaskTimer(@NotNull AbstractPlugin plugin, @NotNull Runnable runnable, long delay, long period) {
        return Bukkit.getScheduler().runTaskTimer(plugin, runnable, FormatUtils.convertDurationToTick(delay), FormatUtils.convertDurationToTick(period));
    }

    public static @NotNull BukkitTask runTaskTimerAsynchronously(@NotNull AbstractPlugin plugin, @NotNull Runnable runnable, long delay, long period) {
        return Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, runnable, FormatUtils.convertDurationToTick(delay), FormatUtils.convertDurationToTick(period));
    }
}
