package org.huanshi.mc.framework.timer;

import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownerHelper {
    protected final boolean reentry;
    protected final long duration;
    protected final Map<UUID, Cooldowner> cooldownerMap = new HashMap<>();

    public CooldownerHelper(boolean reentry, long duration) {
        this.reentry = reentry;
        this.duration = duration;
    }

    public boolean start(@NotNull Entity entity) {
        return cooldownerMap.computeIfAbsent(entity.getUniqueId(), uuid -> new Cooldowner(false, duration) {
            @Override
            protected boolean onReentry() {
                return CooldownerHelper.this.onReentry();
            }
            @Override
            protected boolean onStart() {
                return CooldownerHelper.this.onStart();
            }
            @Override
            protected boolean onRun(long durationLeft) {
                return CooldownerHelper.this.onRun(durationLeft);
            }
            @Override
            protected boolean onStop() {
                return CooldownerHelper.this.onStop();
            }
        }).start();
    }

    public void stop(@NotNull UUID uuid) {
        Cooldowner cooldowner = cooldownerMap.get(uuid);
        if (cooldowner != null) {
            cooldowner.stop();
        }
    }

    public void stop(@NotNull Entity entity) {
        stop(entity.getUniqueId());
    }

    public boolean isRunning(@NotNull UUID uuid) {
        Cooldowner cooldowner = cooldownerMap.get(uuid);
        if (cooldowner == null) {
            return false;
        }
        return cooldowner.isRunning();
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

    protected boolean onRun(long durationLeft) {
        return true;
    }

    protected boolean onStop() {
        return true;
    }
}
