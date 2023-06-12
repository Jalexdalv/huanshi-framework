package org.huanshi.mc.framework.service;

import org.huanshi.mc.framework.plugin.AbstractPlugin;
import org.huanshi.mc.framework.pojo.IComponent;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractService implements IComponent {
    @Override
    public void onCreate(@NotNull AbstractPlugin plugin) {}

    @Override
    public void onLoad(@NotNull AbstractPlugin plugin) {}
}
