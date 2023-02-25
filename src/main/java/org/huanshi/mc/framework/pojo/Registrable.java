package org.huanshi.mc.framework.pojo;

import org.huanshi.mc.framework.AbstractPlugin;
import org.jetbrains.annotations.NotNull;

public interface Registrable {
    void register(@NotNull AbstractPlugin plugin) throws Throwable;
}
