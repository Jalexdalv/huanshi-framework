package org.huanshi.mc.framework.timer;

import org.bukkit.scheduler.BukkitRunnable;
import org.huanshi.mc.framework.AbstractPlugin;
import org.huanshi.mc.framework.api.BukkitAPI;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class CountdownHelper {
    private final Map<UUID, Integer> repeatMap = new HashMap<>();
    private final AbstractPlugin plugin;

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
        AtomicInteger atomicInteger = new AtomicInteger(repeat);
        BukkitRunnable bukkitRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                if ((atomicInteger.getAndDecrement() <= 0L || (runHandler != null && !runHandler.handle(atomicInteger.get()))) && (stopHandler == null || stopHandler.handle())) {
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

    public void start(@NotNull UUID uuid, boolean async, boolean reentry, int repeat, long delay, long period, @Nullable IReentryHandler reentryHandler, @Nullable IStartHandler startHandler, @Nullable IRunHandler runHandler, @Nullable IStopHandler stopHandler) {
        synchronized (getClass() + uuid.toString()) {
            Integer repeatLeft = repeatMap.get(uuid);
            if (repeatLeft != null && repeatLeft > 0L) {
                if (reentry && (reentryHandler == null || reentryHandler.handle())) {
                    setup(uuid, repeat);
                }
            } else if (startHandler == null || startHandler.handle()) {
                setup(uuid, repeat);
                BukkitRunnable bukkitRunnable = new BukkitRunnable() {
                    @Override
                    public void run() {
                        synchronized (getClass() + uuid.toString()) {
                            Integer repeatLeft = repeatMap.get(uuid);
                            if (repeatLeft != null && repeatLeft > 0L) {
                                if (runHandler == null || runHandler.handle(getRepeatLeft(uuid))) {
                                    repeatMap.put(uuid, Math.max(--repeatLeft, 0));
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
            repeatMap.remove(uuid);
        }
    }

    private void setup(@NotNull UUID uuid, int repeat) {
        repeatMap.put(uuid, repeat);
    }

    public int getRepeatLeft(@NotNull UUID uuid) {
        synchronized (getClass() + uuid.toString()) {
            return repeatMap.getOrDefault(uuid, 0);
        }
    }

    public boolean isRunning(@NotNull UUID uuid) {
        synchronized (getClass() + uuid.toString()) {
            Integer repeat = repeatMap.get(uuid);
            return repeat != null && repeat > 0;
        }
    }
}
