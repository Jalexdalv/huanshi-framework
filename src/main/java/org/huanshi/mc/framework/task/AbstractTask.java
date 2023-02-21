package org.huanshi.mc.framework.task;

import org.bukkit.scheduler.BukkitRunnable;
import org.huanshi.mc.framework.AbstractPlugin;
import org.huanshi.mc.framework.api.BukkitApi;
import org.huanshi.mc.framework.engine.Component;
import org.huanshi.mc.framework.engine.Registrable;
import org.huanshi.mc.framework.annotation.Autowired;
import org.huanshi.mc.framework.annotation.Task;

public abstract class AbstractTask extends BukkitRunnable implements Component, Registrable {
    @Autowired
    private AbstractPlugin plugin;
    protected boolean async;
    protected long delay, period;

    @Override
    public abstract void run();

    @Override
    public final void onCreate() {
        Task task = getClass().getAnnotation(Task.class);
        async = task.async();
        delay = task.delay();
        period = task.period();
    }

    @Override
    public void onLoad() {}

    @Override
    public final void register() {
        if (async) {
            BukkitApi.runTaskTimerAsynchronously(plugin, this, delay, period);
        } else {
            BukkitApi.runTaskTimer(plugin, this, delay, period);
        }
    }

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
