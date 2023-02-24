package org.huanshi.mc.framework.timer;

public class Cooldowner {
    protected final boolean reentry;
    protected final long duration;
    protected long time;

    public Cooldowner(boolean reentry, long duration) {
        this.reentry = reentry;
        this.duration = duration;
    }

    public boolean start() {
        if (isRunning()) {
            if (!reentry) {
                return onRun(getDurationLeft());
            }
            if (onReentry()) {
                setup();
                return true;
            }
        } else if (onStart()) {
            setup();
            return true;
        }
        return false;
    }

    public boolean stop() {
        time = 0L;
        return onStop();
    }

    private void setup() {
        time = System.currentTimeMillis() + duration;
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

    public long getDurationLeft() {
        return Math.max(time - System.currentTimeMillis(), 0L);
    }

    public boolean isRunning() {
        return time - System.currentTimeMillis() > 0L;
    }
}
