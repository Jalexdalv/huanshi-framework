package org.huanshi.mc.framework.task;

import org.bukkit.scheduler.BukkitRunnable;
import org.huanshi.mc.framework.AbstractPlugin;
import org.huanshi.mc.framework.pojo.IComponent;
import org.huanshi.mc.framework.pojo.Registrable;
import org.huanshi.mc.framework.annotation.HuanshiTask;
import org.huanshi.mc.framework.utils.FormatUtils;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractTask extends BukkitRunnable implements IComponent, Registrable {
    private final @NotNull HuanshiTask huanshiTask = getClass().getAnnotation(HuanshiTask.class);
    private final boolean async = huanshiTask.async();
    private final long delay = huanshiTask.delay(), period = huanshiTask.period();

    @Override
    public abstract void run();

    @Override
    public void onCreate(@NotNull AbstractPlugin plugin) {}

    @Override
    public void onLoad(@NotNull AbstractPlugin plugin) {}

    @Override
    public void register(@NotNull AbstractPlugin plugin) {
        if (async) {
            runTaskTimerAsynchronously(plugin, FormatUtils.convertDurationToTick(delay), FormatUtils.convertDurationToTick(period));
        } else {
            runTaskTimer(plugin, FormatUtils.convertDurationToTick(delay), FormatUtils.convertDurationToTick(period));
        }
    }
}
