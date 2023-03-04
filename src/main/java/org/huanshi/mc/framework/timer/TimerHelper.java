package org.huanshi.mc.framework.timer;

import org.bukkit.scheduler.BukkitRunnable;
import org.huanshi.mc.framework.AbstractPlugin;
import org.huanshi.mc.framework.utils.FormatUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TimerHelper {
    private final AbstractPlugin plugin;
    private final Map<UUID, Long> durationMap = new HashMap<>();

    public interface IReentryHandler {
        boolean handle();
    }

    public interface IStartHandler {
        boolean handle();
    }

    public interface IRunHandler {
        boolean handle(long repeatLeft);
    }

    public interface IStopHandler {
        boolean handle();
    }

    public TimerHelper(@NotNull AbstractPlugin plugin) {
        this.plugin = plugin;
    }

    public static void start(@NotNull AbstractPlugin plugin, boolean async, long duration, long delay, long period, @Nullable IStartHandler startHandler, @Nullable IRunHandler runHandler, @Nullable IStopHandler stopHandler) {
        if (startHandler == null || startHandler.handle()) {
            BukkitRunnable bukkitRunnable = new BukkitRunnable() {
                private final AtomicLong atomicLong = new AtomicLong(duration);
                @Override
                public void run() {
                    long repeatLeft = atomicLong.getAndAdd(-period);
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

    public void start(@NotNull UUID uuid, boolean async, boolean reentry, long duration, long delay, long period, @Nullable IReentryHandler reentryHandler, @Nullable IStartHandler startHandler, @Nullable IRunHandler runHandler, @Nullable IStopHandler stopHandler) {
        synchronized (getClass() + uuid.toString()) {
            if (durationMap.containsKey(uuid)) {
                if (reentry && (reentryHandler == null || reentryHandler.handle())) {
                    init(uuid, duration);
                }
            } else if (startHandler == null || startHandler.handle()) {
                init(uuid, duration);
                BukkitRunnable bukkitRunnable = new BukkitRunnable() {
                    @Override
                    public void run() {
                        synchronized (getClass() + uuid.toString()) {
                            long durationLeft = durationMap.getOrDefault(uuid, 0L);
                            if (durationLeft > 0L) {
                                if (runHandler == null || runHandler.handle(durationLeft)) {
                                    durationMap.put(uuid, durationLeft - period);
                                }
                            } else if (stopHandler == null || stopHandler.handle()) {
                                durationMap.remove(uuid);
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
            durationMap.remove(uuid);
        }
    }

    private void init(@NotNull UUID uuid, long duration) {
        durationMap.put(uuid, duration);
    }

    public boolean isRunning(@NotNull UUID uuid) {
        synchronized (getClass() + uuid.toString()) {
            return durationMap.containsKey(uuid);
        }
    }
}
