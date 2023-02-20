package org.huanshi.mc.framework.utils;

public class FormatUtils {
    public static long convertDurationToTick(long duration) {
        return (long) Math.ceil((double) duration / (double) 50);
    }

    public static int convertMillisecondToSecond(long millisecond) {
        return (int) Math.ceil((double) millisecond / (double) 1000);
    }
}
