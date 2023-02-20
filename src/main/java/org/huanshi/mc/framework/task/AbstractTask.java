package org.huanshi.mc.framework.task;

import org.bukkit.scheduler.BukkitRunnable;
import org.huanshi.mc.framework.AbstractPlugin;
import org.huanshi.mc.framework.engine.Component;
import org.huanshi.mc.framework.engine.Registrable;
import org.huanshi.mc.framework.annotation.Autowired;
import org.huanshi.mc.framework.annotation.Task;

public abstract class AbstractTask extends BukkitRunnable implements Component, Registrable {
    @Autowired
    private AbstractPlugin plugin;
    protected final boolean async;
    protected final long delay, period;

    public AbstractTask() {
        final Task task = getClass().getAnnotation(Task.class);
        async = task.async();
        delay = task.delay();
        period = task.period();
    }

    @Override
    public void load() {}

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

    public final boolean isAsync() {
        return async;
    }

    public final long getDelay() {
        return delay;
    }

    public final long getPeriod() {
        return period;
    }
}
