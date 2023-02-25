package org.huanshi.mc.framework.timer;

import org.bukkit.scheduler.BukkitRunnable;
import org.huanshi.mc.framework.AbstractPlugin;
import org.huanshi.mc.framework.utils.FormatUtils;
import org.jetbrains.annotations.NotNull;

public class Timer extends BukkitRunnable {
    private final AbstractPlugin plugin;
    private final boolean async, reentry;
    private final long duration, delay, period;
    private long durationLeft;

    public Timer(@NotNull AbstractPlugin plugin, boolean async, boolean reentry, long duration, long delay, long period) {
        this.plugin = plugin;
        this.async = async;
        this.reentry = reentry;
        this.duration = duration;
        this.delay = delay;
        this.period = period;
    }

    @Override
    public void run() {
        if (isRunning() && onRun(getDurationLeft())) {
            durationLeft = Math.max(durationLeft - period, 0L);
        } else if (onStop()) {
            cancel();
        }
    }

    public synchronized void start() {
        if (isRunning()) {
            if (reentry && onReentry()) {
                setup();
            }
        } else if (onStart()) {
            setup();
            if (async) {
                runTaskTimerAsynchronously(plugin, FormatUtils.convertDurationToTick(delay), FormatUtils.convertDurationToTick(period));
            } else {
                runTaskTimer(plugin, FormatUtils.convertDurationToTick(delay), FormatUtils.convertDurationToTick(period));
            }
        }
    }

    public synchronized void stop() {
        durationLeft = 0L;
    }

    private void setup() {
        durationLeft = duration;
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

    public synchronized long getDurationLeft() {
        return durationLeft;
    }

    public synchronized boolean isRunning() {
        return durationLeft > 0L;
    }
}
