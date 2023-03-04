package org.huanshi.mc.framework.timer;

import org.bukkit.scheduler.BukkitRunnable;
import org.huanshi.mc.framework.AbstractPlugin;
import org.huanshi.mc.framework.utils.FormatUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class CountdownHelper {
    private final AbstractPlugin plugin;
    private final Map<UUID, Integer> repeatMap = new HashMap<>();

    public interface IReentryHandler {
        boolean handle();
    }

    public interface IStartHandler {
        boolean handle();
    }

    public interface IRunHandler {
        boolean handle(int repeatLeft);
    }

    public interface IStopHandler {
        boolean handle();
    }

    public CountdownHelper(@NotNull AbstractPlugin plugin) {
        this.plugin = plugin;
    }

    public static void start(@NotNull AbstractPlugin plugin, boolean async, int repeat, long delay, long period, @Nullable IStartHandler startHandler, @Nullable IRunHandler runHandler, @Nullable IStopHandler stopHandler) {
        if (startHandler == null || startHandler.handle()) {
            BukkitRunnable bukkitRunnable = new BukkitRunnable() {
                private final AtomicInteger atomicInteger = new AtomicInteger(repeat);
                @Override
                public void run() {
                    int repeatLeft = atomicInteger.getAndDecrement();
                    if ((repeatLeft <= 0L || (runHandler != null && !runHandler.handle(repeatLeft))) && (stopHandler == null || stopHandler.handle())) {
                        cancel();
                    }
                }
            };
            if (async) {
                bukkitRunnable.runTaskTimerAsynchronously(plugin, FormatUtils.convertDurationToTick(delay), FormatUtils.convertDurationToTick(period));
            } else {
                bukkitRunnable.runTaskTimer(plugin, FormatUtils.convertDurationToTick(delay), FormatUtils.convertDurationToTick(period));
            }
        }
    }

    public void start(@NotNull UUID uuid, boolean async, boolean reentry, int repeat, long delay, long period, @Nullable IReentryHandler reentryHandler, @Nullable IStartHandler startHandler, @Nullable IRunHandler runHandler, @Nullable IStopHandler stopHandler) {
        synchronized (getClass() + uuid.toString()) {
            if (repeatMap.containsKey(uuid)) {
                if (reentry && (reentryHandler == null || reentryHandler.handle())) {
                    init(uuid, repeat);
                }
            } else if (startHandler == null || startHandler.handle()) {
                init(uuid, repeat);
                BukkitRunnable bukkitRunnable = new BukkitRunnable() {
                    @Override
                    public void run() {
                        synchronized (getClass() + uuid.toString()) {
                            int repeatLeft = repeatMap.getOrDefault(uuid, 0);
                            if (repeatLeft > 0) {
                                if (runHandler == null || runHandler.handle(repeatLeft)) {
                                    repeatMap.put(uuid, --repeatLeft);
                                }
                            } else if (stopHandler == null || stopHandler.handle()) {
                                cancel();
                            }
                        }
                    }
                };
                if (async) {
                    bukkitRunnable.runTaskTimerAsynchronously(plugin, FormatUtils.convertDurationToTick(delay), FormatUtils.convertDurationToTick(period));
                } else {
                    bukkitRunnable.runTaskTimer(plugin, FormatUtils.convertDurationToTick(delay), FormatUtils.convertDurationToTick(period));
                }
            }
        }
    }

    public void stop(@NotNull UUID uuid) {
        synchronized (getClass() + uuid.toString()) {
            repeatMap.remove(uuid);
        }
    }

    private void init(@NotNull UUID uuid, int repeat) {
        repeatMap.put(uuid, repeat);
    }

    public boolean isRunning(@NotNull UUID uuid) {
        synchronized (getClass() + uuid.toString()) {
            return repeatMap.containsKey(uuid);
        }
    }
}
