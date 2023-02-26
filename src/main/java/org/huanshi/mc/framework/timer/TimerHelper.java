package org.huanshi.mc.framework.timer;

import org.bukkit.scheduler.BukkitRunnable;
import org.huanshi.mc.framework.AbstractPlugin;
import org.huanshi.mc.framework.api.BukkitAPI;
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
            BukkitAPI.runTaskTimerAsynchronously(plugin, bukkitRunnable, delay, period);
        } else {
            BukkitAPI.runTaskTimer(plugin, bukkitRunnable, delay, period);
        }
    }

    public void start(@NotNull UUID uuid, boolean async, boolean reentry, long duration, long delay, long period, @Nullable IReentryHandler reentryHandler, @Nullable IStartHandler startHandler, @Nullable IRunHandler runHandler, @Nullable IStopHandler stopHandler) {
        synchronized (getClass() + uuid.toString()) {
            Long durationLeft = durationMap.get(uuid);
            if (durationLeft != null && durationLeft > 0L) {
                if (reentry && (reentryHandler == null || reentryHandler.handle())) {
                    setup(uuid, duration);
                }
            } else if (startHandler == null || startHandler.handle()) {
                setup(uuid, duration);
                BukkitRunnable bukkitRunnable = new BukkitRunnable() {
                    @Override
                    public void run() {
                        synchronized (getClass() + uuid.toString()) {
                            Long durationLeft = durationMap.get(uuid);
                            if (durationLeft != null && durationLeft > 0L) {
                                if (runHandler == null || runHandler.handle(getDurationLeft(uuid))) {
                                    durationMap.put(uuid, Math.max(durationLeft - period, 0));
                                }
                            } else if (stopHandler == null || stopHandler.handle()) {
                                cancel();
                            }
                        }
                    }
                };
                if (async) {
                    BukkitAPI.runTaskTimerAsynchronously(plugin, bukkitRunnable, delay, period);
                } else {
                    BukkitAPI.runTaskTimer(plugin, bukkitRunnable, delay, period);
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
            Long duration = durationMap.get(uuid);
            return duration != null && duration > 0L;
        }
    }
}
