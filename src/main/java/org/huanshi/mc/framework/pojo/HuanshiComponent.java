package org.huanshi.mc.framework.pojo;

import org.huanshi.mc.framework.AbstractPlugin;
import org.jetbrains.annotations.NotNull;

public interface HuanshiComponent {
    void onCreate(@NotNull AbstractPlugin plugin) throws Throwable;
    void onLoad(@NotNull AbstractPlugin plugin)  throws Throwable;
}
