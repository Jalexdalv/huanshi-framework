package org.huanshi.mc.framework.timer;

import org.bukkit.scheduler.BukkitRunnable;
import org.huanshi.mc.framework.AbstractPlugin;
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
    public void run() {
        if (isRunning() && onRun(getRepeatLeft())) {
            repeatLeft--;
        } else if (onStop()) {
            cancel();
        }
    }

    public void start() {
        if (isRunning()) {
            if (reentry) {
                if (onReentry()) {
                    repeatLeft = repeat;
                }
            }
        } else if (onStart()) {
            repeatLeft = repeat;
            if (async) {
                runTaskTimerAsynchronously(plugin, delay, period);
            } else {
                runTaskTimer(plugin, delay, period);
            }
        }
    }

    public void stop() {
        repeatLeft = 0;
    }

    private boolean onReentry() {
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

    public int getRepeatLeft() {
        return repeatLeft;
    }

    public boolean isRunning() {
        return repeatLeft > 0;
    }
}
