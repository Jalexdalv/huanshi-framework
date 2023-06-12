package org.huanshi.mc.framework.pojo;

import org.huanshi.mc.framework.plugin.AbstractPlugin;
import org.jetbrains.annotations.NotNull;

public interface IRegistrar {
    void register(@NotNull AbstractPlugin plugin) throws Throwable;
}
