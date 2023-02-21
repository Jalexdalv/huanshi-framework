package org.huanshi.mc.framework.timer;

import org.bukkit.scheduler.BukkitRunnable;
import org.huanshi.mc.framework.AbstractPlugin;
import org.jetbrains.annotations.NotNull;

public class Timer extends BukkitRunnable {
    protected final AbstractPlugin plugin;
    protected final boolean async, reentry;
    protected final long duration, delay, period;
    protected long durationLeft;

    public Timer(@NotNull AbstractPlugin plugin, boolean async, boolean reentry, long duration, long delay, long period) {
        this.plugin = plugin;
        this.async = async;
        this.reentry = reentry;
        this.duration = duration;
        this.delay = delay;
        this.period = period;
    }

    @Override
    public final void run() {
        if (isRunning() && onRun(getDurationLeft())) {
            durationLeft = durationLeft - period;
        } else if (onStop()) {
            cancel();
        }
    }

    public final void start() {
        if (isRunning()) {
            if (reentry) {
                if (onReentry()) {
                    durationLeft = duration;
                }
            }
        } else if (onStart()) {
            durationLeft = duration;
            if (async) {
                runTaskTimerAsynchronously(plugin, delay, period);
            } else {
                runTaskTimer(plugin, delay, period);
            }
        }
    }

    public final void stop() {
        durationLeft = 0L;
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

    public final long getDurationLeft() {
        return durationLeft;
    }

    public final boolean isRunning() {
        return durationLeft > 0L;
    }
}
