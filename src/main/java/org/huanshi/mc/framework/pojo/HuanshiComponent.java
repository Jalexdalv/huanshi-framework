package org.huanshi.mc.framework.pojo;

import org.huanshi.mc.framework.HuanshiPlugin;
import org.jetbrains.annotations.NotNull;

public interface HuanshiComponent {
    void onCreate(@NotNull HuanshiPlugin huanshiPlugin) throws Throwable;
    void onLoad(@NotNull HuanshiPlugin huanshiPlugin)  throws Throwable;
}
