package org.huanshi.mc.framework.timer;

public class Cooldowner {
    protected final boolean reentry;
    protected final long duration;
    protected long time;

    public Cooldowner(boolean reentry, long duration) {
        this.reentry = reentry;
        this.duration = duration;
    }

    public final void start() {
        if (isRunning()) {
            if (reentry) {
                if (onReentry()) {
                    time = System.currentTimeMillis() + duration;
                }
            } else {
                onRun(getDurationLeft());
            }
        } else if (onStart()) {
            time = System.currentTimeMillis() + duration;
        }
    }

    public final void stop() {
        time = 0L;
    }

    protected boolean onReentry() {
        return true;
    }

    protected boolean onStart() {
        return true;
    }

    protected void onRun(long durationLeft) {}

    public final long getDurationLeft() {
        return Math.max(time - System.currentTimeMillis(), 0L);
    }

    public final boolean isRunning() {
        return time - System.currentTimeMillis() > 0L;
    }
}
