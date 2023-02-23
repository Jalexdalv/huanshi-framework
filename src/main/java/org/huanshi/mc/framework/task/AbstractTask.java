package org.huanshi.mc.framework.task;

import lombok.Getter;
import org.bukkit.scheduler.BukkitRunnable;
import org.huanshi.mc.framework.AbstractPlugin;
import org.huanshi.mc.framework.api.BukkitAPI;
import org.huanshi.mc.framework.pojo.HuanshiComponent;
import org.huanshi.mc.framework.pojo.Registrable;
import org.huanshi.mc.framework.annotation.Task;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractTask extends BukkitRunnable implements HuanshiComponent, Registrable {
    protected final @NotNull Task task = getClass().getAnnotation(Task.class);
    @Getter
    protected final boolean async = task.async();
    @Getter
    protected final long delay = task.delay(), period = task.period();

    @Override
    public abstract void run();

    @Override
    public void create(@NotNull AbstractPlugin plugin) {}

    @Override
    public void load(@NotNull AbstractPlugin plugin) {}

    @Override
    public void register(@NotNull AbstractPlugin plugin) {
        if (async) {
            BukkitAPI.runTaskTimerAsynchronously(plugin, this, delay, period);
        } else {
            BukkitAPI.runTaskTimer(plugin, this, delay, period);
        }
    }
}
