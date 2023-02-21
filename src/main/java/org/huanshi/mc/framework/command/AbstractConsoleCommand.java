package org.huanshi.mc.framework.command;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.huanshi.mc.framework.annotation.ConsoleCommand;
import org.huanshi.mc.framework.lang.Zh;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public abstract non-sealed class AbstractConsoleCommand extends AbstractCommand {
    @Override
    public final void onCreate() {
        ConsoleCommand consoleCommand = getClass().getAnnotation(ConsoleCommand.class);
        name = Objects.requireNonNull(StringUtils.trimToNull(consoleCommand.name()));
        for (int i = 0, len = consoleCommand.args().length; i < len; i++) {
            consoleCommand.args()[i] = Objects.requireNonNull(StringUtils.trimToNull(consoleCommand.args()[i]));
        }
        args = consoleCommand.args();
    }

    @Override
    public final boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String name, @NotNull String @NotNull [] args) {
        if (commandSender instanceof ConsoleCommandSender consoleCommandSender) {
            return onConsoleCommand(consoleCommandSender, args);
        }
        commandSender.sendMessage(Zh.ONLY_CONSOLE);
        return true;
    }

    protected abstract boolean onConsoleCommand(@NotNull ConsoleCommandSender consoleCommandSender, @NotNull String @NotNull [] args);
}
