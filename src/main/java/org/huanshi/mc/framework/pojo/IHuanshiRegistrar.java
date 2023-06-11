package org.huanshi.mc.framework.pojo;

import org.huanshi.mc.framework.AbstractHuanshiPlugin;
import org.jetbrains.annotations.NotNull;

public interface IHuanshiRegistrar {
    void register(@NotNull AbstractHuanshiPlugin huanshiPlugin) throws Throwable;
}
