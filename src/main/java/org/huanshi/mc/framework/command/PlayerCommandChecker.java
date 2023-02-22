package org.huanshi.mc.framework.command;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.huanshi.mc.framework.engine.Component;
import org.huanshi.mc.framework.event.ComponentScanCompleteEvent;
import org.huanshi.mc.framework.listener.AbstractListener;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PlayerCommandChecker extends AbstractListener {
    private Set<String> playerCommandNameSet = new HashSet<>();
    private Map<String, AbstractPlayerCommand> commonPlayerCommandMap = new HashMap<>(), opPlayerCommandMap = new HashMap<>();

    @EventHandler
    public void onComponentScanComplete(@NotNull ComponentScanCompleteEvent componentScanCompleteEvent) {
        for (Component component : componentScanCompleteEvent.getComponents()) {
            if (component instanceof AbstractPlayerCommand playerCommand) {
                String name = playerCommand.getName();
                playerCommandNameSet.add(name);
                if (playerCommand.isOp()) {
                    opPlayerCommandMap.put(name, playerCommand);
                } else {
                    commonPlayerCommandMap.put(name, playerCommand);
                }
            }
        }
        playerCommandNameSet = Collections.unmodifiableSet(playerCommandNameSet);
        commonPlayerCommandMap = Collections.unmodifiableMap(commonPlayerCommandMap);
        opPlayerCommandMap = Collections.unmodifiableMap(opPlayerCommandMap);
    }

    public boolean check(@NotNull Player player, @NotNull String name) {
        if (player.isOp()) {
            return true;
        }
        return commonPlayerCommandMap.containsKey(name);
    }

    public @NotNull Collection<String> getPlayerCommandNames(@NotNull Player player) {
        if (player.isOp()) {
            return playerCommandNameSet;
        }
        List<String> playerCommandNameList = new LinkedList<>();
        for (AbstractPlayerCommand playerCommand : commonPlayerCommandMap.values()) {
            if (playerCommand.hasPermission(player)) {
                playerCommandNameList.add(playerCommand.getName());
            }
        }
        return playerCommandNameList;
    }
}
