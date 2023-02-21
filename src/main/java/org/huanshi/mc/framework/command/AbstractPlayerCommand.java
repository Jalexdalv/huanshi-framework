package org.huanshi.mc.framework.command;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.huanshi.mc.framework.annotation.PlayerCommand;
import org.huanshi.mc.framework.api.BukkitApi;
import org.huanshi.mc.framework.lang.Zh;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public abstract non-sealed class AbstractPlayerCommand extends AbstractCommand implements TabExecutor {
    protected boolean op;
    protected String permission;
    protected final List<String> emptyTabList = List.of();

    @Override
    public final void onCreate() {
        PlayerCommand playerCommand = getClass().getAnnotation(PlayerCommand.class);
        op = playerCommand.op();
        permission = StringUtils.trimToNull(playerCommand.permission());
        name = Objects.requireNonNull(StringUtils.trimToNull(playerCommand.name()));
        for (int i = 0, len = playerCommand.args().length; i < len; i++) {
            playerCommand.args()[i] = Objects.requireNonNull(StringUtils.trimToNull(playerCommand.args()[i]));
        }
        args = playerCommand.args();
    }

    @Override
    public final void register() {
        BukkitApi.registerTabExecutor(name, this);
    }

    @Override
    public final boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String name, @NotNull String @NotNull [] args) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage(Zh.ONLY_GAME);
            return true;
        }
        if (!canUse(player)) {
            player.sendMessage(Zh.CANNOT_USE_COMMAND);
            return true;
        }
        if (!hasPermission(player)) {
            player.sendMessage(Zh.NO_PERMISSION);
            return true;
        }
        return onPlayerCommand(player, args);
    }

    @Override
    public final @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String name, @NotNull String @NotNull [] args) {
        if (commandSender instanceof Player player) {
            return onPlayerTabComplete(player, args);
        }
        return null;
    }

    protected abstract boolean onPlayerCommand(@NotNull Player player, @NotNull String @NotNull [] args);

    protected abstract @Nullable List<String> onPlayerTabComplete(@NotNull Player player, @NotNull String @NotNull [] args);

    protected boolean canUse(@NotNull Player player) {
        return true;
    }

    protected boolean hasPermission(@NotNull Player player) {
        return player.isOp() || (!op && (permission == null || player.hasPermission(permission)));
    }
}
