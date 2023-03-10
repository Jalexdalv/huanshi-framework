package org.huanshi.mc.framework.pojo;

import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

public interface IEntityHandler<T extends Entity> {
    void handle(@NotNull T t);
}
