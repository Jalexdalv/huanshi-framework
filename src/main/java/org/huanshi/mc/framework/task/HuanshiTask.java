package org.huanshi.mc.framework.task;

import org.bukkit.scheduler.BukkitRunnable;
import org.huanshi.mc.framework.HuanshiPlugin;
import org.huanshi.mc.framework.pojo.HuanshiComponent;
import org.huanshi.mc.framework.pojo.Registrable;
import org.huanshi.mc.framework.annotation.Task;
import org.huanshi.mc.framework.utils.FormatUtils;
import org.jetbrains.annotations.NotNull;

public abstract class HuanshiTask extends BukkitRunnable implements HuanshiComponent, Registrable {
    protected final @NotNull Task task = getClass().getAnnotation(Task.class);
    protected final boolean async = task.async();
    protected final long delay = task.delay(), period = task.period();

    @Override
    public abstract void run();

    @Override
    public void onCreate(@NotNull HuanshiPlugin huanshiPlugin) {}

    @Override
    public void onLoad(@NotNull HuanshiPlugin huanshiPlugin) {}

    @Override
    public void register(@NotNull HuanshiPlugin huanshiPlugin) {
        if (async) {
            runTaskTimerAsynchronously(huanshiPlugin, FormatUtils.convertDurationToTick(delay), FormatUtils.convertDurationToTick(period));
        } else {
            runTaskTimer(huanshiPlugin, FormatUtils.convertDurationToTick(delay), FormatUtils.convertDurationToTick(period));
        }
    }
}
