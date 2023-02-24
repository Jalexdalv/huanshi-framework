package org.huanshi.mc.framework.pojo;

import org.huanshi.mc.framework.HuanshiPlugin;
import org.jetbrains.annotations.NotNull;

public interface HuanshiComponent {
    void onCreate(@NotNull HuanshiPlugin plugin) throws Throwable;
    void onLoad(@NotNull HuanshiPlugin plugin)  throws Throwable;
}
