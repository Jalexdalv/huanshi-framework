package org.huanshi.mc.framework.task;

import org.bukkit.scheduler.BukkitRunnable;
import org.huanshi.mc.framework.AbstractPlugin;
import org.huanshi.mc.framework.engine.Component;
import org.huanshi.mc.framework.engine.Registrable;
import org.huanshi.mc.framework.annotation.Autowired;
import org.huanshi.mc.framework.annotation.Task;
import org.huanshi.mc.framework.utils.FormatUtils;

public abstract class AbstractTask extends BukkitRunnable implements Component, Registrable {
    @Autowired
    private AbstractPlugin plugin;
    protected boolean async;
    protected long delay, period;

    @Override
    public final void onCreate() {
        Task task = getClass().getAnnotation(Task.class);
        async = task.async();
        delay = FormatUtils.convertDurationToTick(task.delay());
        period = FormatUtils.convertDurationToTick(task.period());
    }

    @Override
    public void onLoad() {}

    @Override
    public final void register() {
        if (async) {
            runTaskTimerAsynchronously(plugin, delay, period);
        } else {
            runTaskTimer(plugin, delay, period);
        }
    }

    @Override
    public abstract void run();

    public boolean isAsync() {
        return async;
    }

    public long getDelay() {
        return delay;
    }

    public long getPeriod() {
        return period;
    }
}
