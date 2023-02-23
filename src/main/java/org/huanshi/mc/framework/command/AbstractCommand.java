package org.huanshi.mc.framework.command;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.huanshi.mc.framework.AbstractPlugin;
import org.huanshi.mc.framework.annotation.Command;
import org.huanshi.mc.framework.api.BukkitAPI;
import org.huanshi.mc.framework.pojo.HuanshiComponent;
import org.huanshi.mc.framework.pojo.Registrable;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractCommand implements HuanshiComponent, Registrable, TabExecutor {
    @Getter
    protected final String name = StringUtils.trimToNull(getClass().getAnnotation(Command.class).name());

    @Override
    public void create(@NotNull AbstractPlugin plugin) {}

    @Override
    public void load(@NotNull AbstractPlugin plugin) {}

    @Override
    public void register(@NotNull AbstractPlugin plugin) {
        BukkitAPI.registerTabExecutor(name, this);
    }

    public abstract boolean hasPermission(@NotNull Player player);
}
