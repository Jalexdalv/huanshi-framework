package org.huanshi.mc.framework.timer;

public class Cooldowner {
    protected final boolean reentry;
    protected final long duration;
    protected long time;

    public Cooldowner(boolean reentry, long duration) {
        this.reentry = reentry;
        this.duration = duration;
    }

    public void start() {
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

    public void stop() {
        time = 0L;
    }

    private boolean onReentry() {
        return true;
    }

    private boolean onStart() {
        return true;
    }

    private void onRun(long durationLeft) {}

    public long getDurationLeft() {
        return Math.max(time - System.currentTimeMillis(), 0L);
    }

    public boolean isRunning() {
        return time - System.currentTimeMillis() > 0L;
    }
}
