package org.huanshi.mc.framework.service;

import org.huanshi.mc.framework.AbstractPlugin;
import org.huanshi.mc.framework.pojo.HuanshiComponent;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractService implements HuanshiComponent {
    @Override
    public void create(@NotNull AbstractPlugin plugin) {}

    @Override
    public void load(@NotNull AbstractPlugin plugin) {}
}
