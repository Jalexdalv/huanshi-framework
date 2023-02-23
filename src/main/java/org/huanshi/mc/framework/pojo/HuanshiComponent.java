package org.huanshi.mc.framework.pojo;

import org.huanshi.mc.framework.AbstractPlugin;
import org.jetbrains.annotations.NotNull;

public interface HuanshiComponent {
    void create(@NotNull AbstractPlugin plugin) throws Throwable;
    void load(@NotNull AbstractPlugin plugin)  throws Throwable;
}
