package org.huanshi.mc.framework.command;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.huanshi.mc.framework.annotation.ConsoleCommand;
import org.huanshi.mc.framework.lang.Zh;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public abstract non-sealed class AbstractConsoleCommand extends AbstractCommand {
    protected String head;
    protected String[] args;

    @Override
    public final void onCreate() {
        ConsoleCommand command = getClass().getAnnotation(ConsoleCommand.class);
        head = Objects.requireNonNull(StringUtils.trimToNull(command.head()));
        for (int i = 0, len = command.args().length; i < len; i++) {
            command.args()[i] = Objects.requireNonNull(StringUtils.trimToNull(command.args()[i]));
        }
        args = command.args();
    }

    @Override
    public void onLoad() {}

    @Override
    public final void register() {
        Objects.requireNonNull(Bukkit.getPluginCommand(head)).setExecutor(this);
    }

    @Override
    public final boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String head, @NotNull String @NotNull [] args) {
        if (commandSender instanceof ConsoleCommandSender consoleCommandSender) {
            return onConsoleCommand(consoleCommandSender, args);
        }
        commandSender.sendMessage(Zh.ONLY_CONSOLE);
        return true;
    }

    protected abstract boolean onConsoleCommand(@NotNull ConsoleCommandSender consoleCommandSender, @NotNull String @NotNull [] args);

    public final @NotNull String getHead() {
        return head;
    }

    public final @NotNull String[] getArgs() {
        return args;
    }
}
