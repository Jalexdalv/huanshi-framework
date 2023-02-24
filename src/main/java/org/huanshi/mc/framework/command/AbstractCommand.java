package org.huanshi.mc.framework.command;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.TabExecutor;
import org.huanshi.mc.framework.HuanshiPlugin;
import org.huanshi.mc.framework.annotation.Command;
import org.huanshi.mc.framework.api.BukkitAPI;
import org.huanshi.mc.framework.pojo.HuanshiComponent;
import org.huanshi.mc.framework.pojo.Registrable;
import org.jetbrains.annotations.NotNull;

public abstract sealed class AbstractCommand implements HuanshiComponent, Registrable, TabExecutor permits HuanshiCommand {
    @Getter
    protected final @NotNull String name = StringUtils.trimToNull(getClass().getAnnotation(Command.class).name());

    @Override
    public void onCreate(@NotNull HuanshiPlugin huanshiPlugin) {}

    @Override
    public void onLoad(@NotNull HuanshiPlugin huanshiPlugin) {}

    @Override
    public void register(@NotNull HuanshiPlugin huanshiPlugin) {
        BukkitAPI.registerTabExecutor(name, this);
    }
}
