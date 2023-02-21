package org.huanshi.mc.framework.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.huanshi.mc.framework.engine.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class ComponentScanCompleteEvent extends Event {
    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final Map<Class<? extends Component>, Component> componentMap;

    public ComponentScanCompleteEvent(@NotNull Map<Class<? extends Component>, Component> componentMap) {
        this.componentMap = componentMap;
    }

    public static @NotNull HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public @NotNull Map<Class<? extends Component>, Component> getComponentMap() {
        return componentMap;
    }
}
