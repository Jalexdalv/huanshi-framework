package org.huanshi.mc.framework.service;

import org.huanshi.mc.framework.AbstractHuanshiPlugin;
import org.huanshi.mc.framework.pojo.IHuanshiComponent;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractHuanshiService implements IHuanshiComponent {
    @Override
    public void onCreate(@NotNull AbstractHuanshiPlugin huanshiPlugin) {}

    @Override
    public void onLoad(@NotNull AbstractHuanshiPlugin huanshiPlugin) {}
}
