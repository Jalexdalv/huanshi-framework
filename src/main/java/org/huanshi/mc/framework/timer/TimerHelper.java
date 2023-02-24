package org.huanshi.mc.framework.timer;

import org.bukkit.entity.Entity;
import org.huanshi.mc.framework.AbstractPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TimerHelper {
    protected final AbstractPlugin plugin;
    protected final boolean async, reentry;
    protected final long duration, delay, period;
    protected final Map<UUID, Timer> timerMap = new HashMap<>();

    public TimerHelper(@NotNull AbstractPlugin plugin, boolean async, boolean reentry, long duration, long delay, long period) {
        this.plugin = plugin;
        this.async = async;
        this.reentry = reentry;
        this.duration = duration;
        this.delay = delay;
        this.period = period;
    }

    public void start(@NotNull Entity entity) {
        timerMap.compute(entity.getUniqueId(), (uuid, timer) -> {
            if (timer == null || !timer.isRunning()) {
                return new Timer(plugin, async, reentry, duration, delay, period) {
                    @Override
                    protected boolean onStart() {
                        return TimerHelper.this.onStart();
                    }
                    @Override
                    protected boolean onRun(long durationLeft) {
                        return TimerHelper.this.onRun(durationLeft);
                    }
                    @Override
                    protected boolean onStop() {
                        return TimerHelper.this.onStop();
                    }
                };
            }
            return timer;
        }).start();
    }

    public void stop(@NotNull UUID uuid) {
        Timer timer = timerMap.get(uuid);
        if (timer != null) {
            timer.stop();
        }
    }

    public void stop(@NotNull Entity entity) {
        stop(entity.getUniqueId());
    }

    public boolean isRunning(@NotNull UUID uuid) {
        Timer timer = timerMap.get(uuid);
        if (timer == null) {
            return false;
        }
        return timer.isRunning();
    }

    public boolean isRunning(@NotNull Entity entity) {
        return isRunning(entity.getUniqueId());
    }

    protected boolean onReentry() {
        return true;
    }

    protected boolean onStart() {
        return true;
    }

    protected boolean onRun(long durationLeft) {
        return true;
    }

    protected boolean onStop() {
        return true;
    }
}
