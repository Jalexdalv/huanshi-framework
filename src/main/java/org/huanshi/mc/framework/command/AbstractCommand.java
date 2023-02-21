package org.huanshi.mc.framework.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.huanshi.mc.framework.engine.Component;
import org.huanshi.mc.framework.engine.Registrable;
import org.jetbrains.annotations.NotNull;

public abstract sealed class AbstractCommand<T extends CommandSender> implements Component, Registrable, CommandExecutor permits AbstractConsoleCommand, AbstractPlayerCommand {
    protected String name;
    protected String[] args;

    @Override
    public void onLoad() {}

    @Override
    public abstract boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String name, @NotNull String @NotNull [] args);

    protected abstract boolean onCommand(@NotNull T t, @NotNull String @NotNull [] args);

    public @NotNull String getName() {
        return name;
    }

    public @NotNull String @NotNull [] getArgs() {
        return args;
    }
}
