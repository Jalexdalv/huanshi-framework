package org.huanshi.mc.framework.engine;

public interface Component {
    void onCreate() throws Throwable;
    void onLoad() throws Throwable;

    default void onReload() throws Throwable {
        onCreate();
        onLoad();
    }
}
