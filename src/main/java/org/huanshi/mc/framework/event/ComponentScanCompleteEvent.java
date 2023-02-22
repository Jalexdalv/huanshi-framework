package org.huanshi.mc.framework.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.huanshi.mc.framework.engine.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class ComponentScanCompleteEvent extends Event {
    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final Collection<Component> componentCollection;

    public ComponentScanCompleteEvent(@NotNull Collection<Component> componentCollection) {
        this.componentCollection = componentCollection;
    }

    public static @NotNull HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public @NotNull Collection<Component> getComponents() {
        return componentCollection;
    }
}
