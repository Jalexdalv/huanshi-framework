package org.huanshi.mc.framework.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.huanshi.mc.framework.engine.Component;
import org.jetbrains.annotations.NotNull;

public class ComponentCreateEvent<T extends Component> extends Event {
    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final T t;

    public ComponentCreateEvent(@NotNull final T t) {
        this.t = t;
    }

    public static @NotNull HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public final @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public final @NotNull T getComponent() {
        return t;
    }
}
