package org.huanshi.mc.framework.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.huanshi.mc.framework.pojo.HuanshiComponent;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class ComponentScanCompleteEvent extends Event {
    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final Collection<HuanshiComponent> huanshiComponentCollection;

    public ComponentScanCompleteEvent(@NotNull Collection<HuanshiComponent> huanshiComponentCollection) {
        this.huanshiComponentCollection = huanshiComponentCollection;
    }

    public static @NotNull HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public @NotNull Collection<HuanshiComponent> getHuanshiComponents() {
        return huanshiComponentCollection;
    }
}
