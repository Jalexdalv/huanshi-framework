package org.huanshi.mc.framework.pojo;

import org.huanshi.mc.framework.HuanshiPlugin;
import org.jetbrains.annotations.NotNull;

public interface Registrable {
    void register(@NotNull HuanshiPlugin huanshiPlugin) throws Throwable;
}
