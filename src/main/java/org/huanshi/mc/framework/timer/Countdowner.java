package org.huanshi.mc.framework.timer;

import org.bukkit.scheduler.BukkitRunnable;
import org.huanshi.mc.framework.AbstractPlugin;
import org.huanshi.mc.framework.utils.FormatUtils;
import org.jetbrains.annotations.NotNull;

public class Countdowner extends BukkitRunnable {
    private final AbstractPlugin plugin;
    private final boolean async, reentry;
    private final int repeat;
    private final long delay, period;
    private int repeatLeft;

    public Countdowner(@NotNull AbstractPlugin plugin, boolean async, boolean reentry, int repeat, long delay, long period) {
        this.plugin = plugin;
        this.async = async;
        this.reentry = reentry;
        this.repeat = repeat;
        this.delay = delay;
        this.period = period;
    }

    @Override
    public void run() {
        if (isRunning() && onRun(getRepeatLeft())) {
            repeatLeft--;
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
        repeatLeft = 0;
    }

    private void setup() {
        repeatLeft = repeat;
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
