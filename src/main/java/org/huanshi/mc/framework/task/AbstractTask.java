package org.huanshi.mc.framework.task;

import org.bukkit.scheduler.BukkitRunnable;
import org.huanshi.mc.framework.plugin.AbstractPlugin;
import org.huanshi.mc.framework.pojo.IComponent;
import org.huanshi.mc.framework.pojo.IRegistrar;
import org.huanshi.mc.framework.annotation.Task;
import org.huanshi.mc.framework.utils.FormatUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public abstract class AbstractTask extends BukkitRunnable implements IComponent, IRegistrar {
    private final Task task = Objects.requireNonNull(getClass().getAnnotation(Task.class));
    private final boolean async = task.async();
    private final long delay = FormatUtils.convertDurationToTick(task.delay()), period = FormatUtils.convertDurationToTick(task.period());

    @Override
    public abstract void run();

    @Override
    public void onCreate(@NotNull AbstractPlugin plugin) {}

    @Override
    public void onLoad(@NotNull AbstractPlugin plugin) {}

    @Override
    public void register(@NotNull AbstractPlugin plugin) {
        if (async) {
            runTaskTimerAsynchronously(plugin, delay, period);
        } else {
            runTaskTimer(plugin, delay, period);
        }
    }
}
