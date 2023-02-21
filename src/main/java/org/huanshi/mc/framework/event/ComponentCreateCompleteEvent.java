package org.huanshi.mc.framework.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.huanshi.mc.framework.engine.Component;
import org.jetbrains.annotations.NotNull;

public class ComponentCreateCompleteEvent extends Event {
    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final Component component;

    public ComponentCreateCompleteEvent(@NotNull Component component) {
        this.component = component;
    }

    public static @NotNull HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public @NotNull Component getComponent() {
        return component;
    }
}
