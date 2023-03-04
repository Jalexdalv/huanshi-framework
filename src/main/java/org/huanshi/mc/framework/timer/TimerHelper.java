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
    private final Map<UUID, Long> durationMap = new HashMap<>();
    private final AbstractPlugin plugin;

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
            AtomicLong atomicLong = new AtomicLong(duration);
            BukkitRunnable bukkitRunnable = new BukkitRunnable() {
                @Override
                public void run() {
                    if ((atomicLong.getAndDecrement() <= 0L || (runHandler != null && !runHandler.handle(atomicLong.get()))) && (stopHandler == null || stopHandler.handle())) {
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
            if (isRunning(uuid)) {
                if (reentry && (reentryHandler == null || reentryHandler.handle())) {
                    setup(uuid, duration);
                }
            } else if (startHandler == null || startHandler.handle()) {
                setup(uuid, duration);
                BukkitRunnable bukkitRunnable = new BukkitRunnable() {
                    @Override
                    public void run() {
                        synchronized (getClass() + uuid.toString()) {
                            long durationLeft = getDurationLeft(uuid);
                            if (durationLeft > 0L) {
                                if (runHandler == null || runHandler.handle(durationLeft)) {
                                    durationMap.put(uuid, Math.max(durationLeft - period, 0L));
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
            durationMap.remove(uuid);
        }
    }

    private void setup(@NotNull UUID uuid, long duration) {
        durationMap.put(uuid, duration);
    }

    public long getDurationLeft(@NotNull UUID uuid) {
        synchronized (getClass() + uuid.toString()) {
            return durationMap.getOrDefault(uuid, 0L);
        }
    }

    public boolean isRunning(@NotNull UUID uuid) {
        synchronized (getClass() + uuid.toString()) {
            return getDurationLeft(uuid) > 0L;
        }
    }
}
