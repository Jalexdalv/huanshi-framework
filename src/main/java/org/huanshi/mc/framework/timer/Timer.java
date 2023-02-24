package org.huanshi.mc.framework.timer;

import org.bukkit.scheduler.BukkitRunnable;
import org.huanshi.mc.framework.HuanshiPlugin;
import org.huanshi.mc.framework.utils.FormatUtils;
import org.jetbrains.annotations.NotNull;

public class Timer extends BukkitRunnable {
    protected final HuanshiPlugin huanshiPlugin;
    protected final boolean async, reentry;
    protected final long duration, delay, period;
    protected long durationLeft;

    public Timer(@NotNull HuanshiPlugin huanshiPlugin, boolean async, boolean reentry, long duration, long delay, long period) {
        this.huanshiPlugin = huanshiPlugin;
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
                runTaskTimerAsynchronously(huanshiPlugin, FormatUtils.convertDurationToTick(delay), FormatUtils.convertDurationToTick(period));
            } else {
                runTaskTimer(huanshiPlugin, FormatUtils.convertDurationToTick(delay), FormatUtils.convertDurationToTick(period));
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
