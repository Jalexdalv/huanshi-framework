package org.huanshi.mc.framework.timer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CoolDownHelper {
    private final Map<UUID, Long> timeMap = new HashMap<>();

    public interface IReentryHandler {
        boolean handle();
    }

    public interface IStartHandler {
        boolean handle();
    }

    public interface IRunHandler {
        void handle(long durationLeft);
    }

    public void start(@NotNull UUID uuid, boolean reentry, long duration, @Nullable IReentryHandler reentryHandler, @Nullable IStartHandler startHandler, @Nullable IRunHandler runHandler) {
        if (isRunning(uuid)) {
            if (reentry) {
                if (reentryHandler == null || reentryHandler.handle()) {
                    setup(uuid, duration);
                }
            } else if (runHandler != null) {
                runHandler.handle(getDurationLeft(uuid));
            }
        } else if (startHandler == null || startHandler.handle()) {
            setup(uuid, duration);
        }
    }

    public void stop(@NotNull UUID uuid) {
        timeMap.remove(uuid);
    }

    private void setup(@NotNull UUID uuid, long duration) {
        timeMap.put(uuid, System.currentTimeMillis() + duration);
    }

    public long getDurationLeft(@NotNull UUID uuid) {
        Long time = timeMap.get(uuid);
        return time == null ? 0L : Math.max(time - System.currentTimeMillis(), 0L);
    }

    public boolean isRunning(@NotNull UUID uuid) {
        Long time = timeMap.get(uuid);
        return time != null && time > System.currentTimeMillis();
    }
}
