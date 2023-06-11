package org.huanshi.mc.framework.task;

import org.bukkit.scheduler.BukkitRunnable;
import org.huanshi.mc.framework.AbstractHuanshiPlugin;
import org.huanshi.mc.framework.pojo.IHuanshiComponent;
import org.huanshi.mc.framework.pojo.IHuanshiRegistrar;
import org.huanshi.mc.framework.annotation.HuanshiTask;
import org.huanshi.mc.framework.utils.FormatUtils;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractHuanshiTask extends BukkitRunnable implements IHuanshiComponent, IHuanshiRegistrar {
    private final HuanshiTask huanshiTask = getClass().getAnnotation(HuanshiTask.class);
    private final boolean async = huanshiTask.async();
    private final long delay = huanshiTask.delay(), period = huanshiTask.period();

    @Override
    public abstract void run();

    @Override
    public void onCreate(@NotNull AbstractHuanshiPlugin huanshiPlugin) {}

    @Override
    public void onLoad(@NotNull AbstractHuanshiPlugin huanshiPlugin) {}

    @Override
    public void register(@NotNull AbstractHuanshiPlugin huanshiPlugin) {
        if (async) {
            runTaskTimerAsynchronously(huanshiPlugin, FormatUtils.convertDurationToTick(delay), FormatUtils.convertDurationToTick(period));
        } else {
            runTaskTimer(huanshiPlugin, FormatUtils.convertDurationToTick(delay), FormatUtils.convertDurationToTick(period));
        }
    }
}
