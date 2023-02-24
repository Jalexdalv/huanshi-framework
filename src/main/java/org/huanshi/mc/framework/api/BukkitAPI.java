package org.huanshi.mc.framework.api;

import lombok.SneakyThrows;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.scheduler.BukkitTask;
import org.huanshi.mc.framework.AbstractPlugin;
import org.huanshi.mc.framework.command.AbstractCommand;
import org.huanshi.mc.framework.listener.AbstractListener;
import org.huanshi.mc.framework.utils.FormatUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Objects;

public class BukkitAPI {
    public static void registerTabExecutor(@NotNull String name, @NotNull AbstractCommand command) {
        PluginCommand pluginCommand = Objects.requireNonNull(Bukkit.getPluginCommand(name));
        pluginCommand.setExecutor(command);
        pluginCommand.setTabCompleter(command);
    }

    public static void registerEvent(@NotNull AbstractPlugin plugin, @NotNull AbstractListener listener) {
        Bukkit.getPluginManager().registerEvents(listener, plugin);
    }

    public static @Nullable Player findPlayer(@NotNull String targetPlayerName) {
        return Bukkit.getPlayerExact(targetPlayerName);
    }

    public static @Nullable World findWorld(@NotNull String worldName) {
        return Bukkit.getWorld(worldName);
    }

    @SneakyThrows
    public static void createDataFolder(@NotNull AbstractPlugin plugin) {
        Path path = plugin.getDataFolder().toPath();
        if (!Files.exists(path)) {
            Files.createDirectory(path);
        }
    }

    public static void sendConsoleMessage(@NotNull Component component) {
        Bukkit.getConsoleSender().sendMessage(component);
    }

    public static void sendLoggerInfo(@NotNull String message) {
        Bukkit.getLogger().info(message);
    }

    public static void cancelAllTasks(@NotNull AbstractPlugin plugin) {
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

    public static @NotNull Collection<? extends Player> getOnlinePlayers() {
        return Bukkit.getOnlinePlayers();
    }
}
