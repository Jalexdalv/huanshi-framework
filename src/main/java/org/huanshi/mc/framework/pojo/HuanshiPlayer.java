package org.huanshi.mc.framework.pojo;

import org.bukkit.craftbukkit.v1_19_R2.CraftServer;
import org.bukkit.craftbukkit.v1_19_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class HuanshiPlayer extends CraftPlayer {
    public HuanshiPlayer(@NotNull Player player) {
        super((CraftServer) player.getServer(), ((CraftPlayer) player).getHandle());
    }
}
