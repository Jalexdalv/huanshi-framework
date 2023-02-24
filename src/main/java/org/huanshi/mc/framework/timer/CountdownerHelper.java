package org.huanshi.mc.framework.timer;

import org.bukkit.entity.Entity;
import org.huanshi.mc.framework.AbstractPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CountdownerHelper {
    protected final AbstractPlugin plugin;
    protected final boolean async, reentry;
    protected final int repeat;
    protected final long delay, period;
    protected final Map<UUID, Countdowner> countdownerMap = new HashMap<>();

    public CountdownerHelper(@NotNull AbstractPlugin plugin, boolean async, boolean reentry, int repeat, long delay, long period) {
        this.plugin = plugin;
        this.async = async;
        this.reentry = reentry;
        this.repeat = repeat;
        this.delay = delay;
        this.period = period;
    }

    public void start(@NotNull Entity entity) {
        countdownerMap.compute(entity.getUniqueId(), (uuid, countdowner) -> {
            if (countdowner == null || !countdowner.isRunning()) {
                return new Countdowner(plugin, async, reentry, repeat, delay, period) {
                    @Override
                    protected boolean onStart() {
                        return CountdownerHelper.this.onStart();
                    }
                    @Override
                    protected boolean onRun(int repeatLeft) {
                        return CountdownerHelper.this.onRun(repeatLeft);
                    }
                    @Override
                    protected boolean onStop() {
                        return CountdownerHelper.this.onStop();
                    }
                };
            }
            return countdowner;
        }).start();
    }

    public void stop(@NotNull UUID uuid) {
        Countdowner countdowner = countdownerMap.get(uuid);
        if (countdowner != null) {
            countdowner.stop();
        }
    }

    public void stop(@NotNull Entity entity) {
        stop(entity.getUniqueId());
    }

    public boolean isRunning(@NotNull UUID uuid) {
        Countdowner countdowner = countdownerMap.get(uuid);
        if (countdowner == null) {
            return false;
        }
        return countdowner.isRunning();
    }

    public boolean isRunning(@NotNull Entity entity) {
        return isRunning(entity.getUniqueId());
    }

    protected boolean onReentry() {
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
}
