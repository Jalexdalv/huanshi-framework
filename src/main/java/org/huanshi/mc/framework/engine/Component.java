package org.huanshi.mc.framework.engine;

public interface Component {
    void create() throws Throwable;
    void load() throws Throwable;

    default void reload() throws Throwable {
        create();
        load();
    }
}
