package org.huanshi.mc.framework.command;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.huanshi.mc.framework.annotation.PlayerCommand;
import org.huanshi.mc.framework.lang.Zh;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract non-sealed class AbstractPlayerCommand extends AbstractCommand implements TabExecutor {
    protected final boolean op;
    protected final String permission, head;
    protected final String[] args;
    protected final List<String> emptyTabList = new ArrayList<>();

    public AbstractPlayerCommand() {
        final PlayerCommand playerCommand = getClass().getAnnotation(PlayerCommand.class);
        op = playerCommand.op();
        permission = StringUtils.trimToNull(playerCommand.permission());
        head = Objects.requireNonNull(StringUtils.trimToNull(playerCommand.head()));
        for (int i = 0, len = playerCommand.args().length; i < len; i++) {
            playerCommand.args()[i] = Objects.requireNonNull(StringUtils.trimToNull(playerCommand.args()[i]));
        }
        args = playerCommand.args();
    }

    @Override
    public void load() {}

    @Override
    public final void register() {
        final PluginCommand pluginCommand = Objects.requireNonNull(Bukkit.getPluginCommand(head));
        pluginCommand.setExecutor(this);
        pluginCommand.setTabCompleter(this);
    }

    @Override
    public final boolean onCommand(@NotNull final CommandSender commandSender, @NotNull final Command command, @NotNull final String head, @NotNull final String @NotNull [] args) {
        if (commandSender instanceof Player player) {
            if (!canUse(player)) {
                player.sendMessage(Zh.CANNOT_USE_COMMAND);
            } else if (!hasPermission(player)) {
                player.sendMessage(Zh.NO_PERMISSION);
            } else {
                return onPlayerCommand(player, args);
            }
        } else {
            commandSender.sendMessage(Zh.ONLY_GAME);
        }
        return true;
    }

    @Override
    public final @Nullable List<String> onTabComplete(@NotNull final CommandSender commandSender, @NotNull final Command command, @NotNull final String head, @NotNull final String @NotNull [] args) {
        if (commandSender instanceof Player player) {
            return onPlayerTabComplete(player, args);
        }
        return null;
    }

    protected abstract boolean onPlayerCommand(@NotNull final Player player, @NotNull final String @NotNull [] args);

    protected abstract @Nullable List<String> onPlayerTabComplete(@NotNull final Player player, @NotNull final String @NotNull [] args);

    protected boolean canUse(@NotNull final Player player) {
        return true;
    }

    protected final boolean hasPermission(@NotNull final Player player) {
        return player.isOp() || (!op && (permission == null || player.hasPermission(permission)));
    }

    public final boolean isOp() {
        return op;
    }

    public final @Nullable String getPermission() {
        return permission;
    }

    public final @NotNull String getHead() {
        return head;
    }

    public final @NotNull String[] getArgs() {
        return args;
    }
}
