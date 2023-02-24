package org.huanshi.mc.framework.service;

import org.huanshi.mc.framework.HuanshiPlugin;
import org.huanshi.mc.framework.pojo.HuanshiComponent;
import org.jetbrains.annotations.NotNull;

public abstract class HuanshiService implements HuanshiComponent {
    @Override
    public void onCreate(@NotNull HuanshiPlugin huanshiPlugin) {}

    @Override
    public void onLoad(@NotNull HuanshiPlugin huanshiPlugin) {}
}
