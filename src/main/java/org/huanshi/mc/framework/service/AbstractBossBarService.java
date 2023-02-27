package org.huanshi.mc.framework.service;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.huanshi.mc.framework.AbstractPlugin;
import org.huanshi.mc.framework.timer.TimerHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class AbstractBossBarService extends AbstractService {
    private TimerHelper timerHelper;
    private final Map<UUID, BossBar> bossBarMap = new HashMap<>();

    public interface IBossBarHandler {
        @NotNull Component handle(long durationLeft);
    }

    @Override
    public void onLoad(@NotNull AbstractPlugin plugin) {
        timerHelper = new TimerHelper(plugin);
    }

    public void start(@NotNull Player player, boolean async, boolean reentry, long duration, long delay, long period, @NotNull BossBar.Color color, @NotNull BossBar.Overlay overlay, @NotNull IBossBarHandler bossBarHandler, @Nullable TimerHelper.IReentryHandler reentryHandler, @Nullable TimerHelper.IStartHandler startHandler, @Nullable TimerHelper.IRunHandler runHandler, @Nullable TimerHelper.IStopHandler stopHandler) {
        UUID uuid = player.getUniqueId();
        timerHelper.start(uuid, async, reentry, duration, delay, period, reentryHandler,
            () -> {
                bossBarMap.putIfAbsent(uuid, BossBar.bossBar(Component.empty(), 1.0F, color, overlay));
                return startHandler == null || startHandler.handle();
            }, durationLeft -> {
                player.showBossBar(bossBarMap.get(uuid).name(bossBarHandler.handle(durationLeft)).progress((float) durationLeft / (float) duration));
                return runHandler == null || runHandler.handle(durationLeft);
            }, () -> {
                player.hideBossBar(bossBarMap.get(uuid));
                return stopHandler == null || stopHandler.handle();
            }
        );
    }

    public void stop(@NotNull UUID uuid) {
        timerHelper.stop(uuid);
    }

    public void stop(@NotNull Player player) {
        stop(player.getUniqueId());
    }

    public boolean isRunning(@NotNull UUID uuid) {
        return timerHelper.isRunning(uuid);
    }

    public boolean isRunning(@NotNull Player player) {
        return isRunning(player.getUniqueId());
    }
}
