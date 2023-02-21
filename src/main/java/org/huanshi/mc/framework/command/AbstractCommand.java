package org.huanshi.mc.framework.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.huanshi.mc.framework.annotation.Autowired;
import org.huanshi.mc.framework.api.BukkitApi;
import org.huanshi.mc.framework.engine.Component;
import org.huanshi.mc.framework.engine.Registrable;
import org.jetbrains.annotations.NotNull;

public abstract sealed class AbstractCommand implements Component, Registrable, CommandExecutor permits AbstractConsoleCommand, AbstractPlayerCommand {
    protected String name;
    protected String[] args;

    @Override
    public void load() {}

    @Override
    public abstract boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String name, @NotNull String @NotNull [] args);

    public @NotNull String getName() {
        return name;
    }

    public @NotNull String[] getArgs() {
        return args;
    }
}
