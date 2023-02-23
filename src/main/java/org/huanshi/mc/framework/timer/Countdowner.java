package org.huanshi.mc.framework.timer;

import org.bukkit.scheduler.BukkitRunnable;
import org.huanshi.mc.framework.AbstractPlugin;
import org.huanshi.mc.framework.utils.FormatUtils;
import org.jetbrains.annotations.NotNull;

public class Countdowner extends BukkitRunnable {
    protected final AbstractPlugin plugin;
    protected final boolean async, reentry;
    protected final int repeat;
    protected final long delay, period;
    protected int repeatLeft;

    public Countdowner(@NotNull AbstractPlugin plugin, boolean async, boolean reentry, int repeat, long delay, long period) {
        this.plugin = plugin;
        this.async = async;
        this.reentry = reentry;
        this.repeat = repeat;
        this.delay = delay;
        this.period = period;
    }

    @Override
    public synchronized void run() {
        if (isRunning() && onRun(getRepeatLeft())) {
            repeatLeft--;
        } else if (onStop()) {
            cancel();
        }
    }

    public synchronized void start() {
        if (isRunning()) {
            if (reentry) {
                if (onReentry()) {
                    repeatLeft = repeat;
                }
            }
        } else if (onStart()) {
            repeatLeft = repeat;
            if (async) {
                runTaskTimerAsynchronously(plugin, FormatUtils.convertDurationToTick(delay), FormatUtils.convertDurationToTick(period));
            } else {
                runTaskTimer(plugin, FormatUtils.convertDurationToTick(delay), FormatUtils.convertDurationToTick(period));
            }
        }
    }

    public synchronized void stop() {
        repeatLeft = 0;
    }

    protected boolean onReentry() {
        return true;
    }

    protected boolean onStart() {
        return true;
    }

    protected boolean onRun(int repeatLeft) {
        return true;
    }

    protected boolean onStop() {
        return true;
    }

    public synchronized int getRepeatLeft() {
        return repeatLeft;
    }

    public synchronized boolean isRunning() {
        return repeatLeft > 0;
    }
}
