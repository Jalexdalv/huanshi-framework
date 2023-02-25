package org.huanshi.mc.framework.event;

import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.huanshi.mc.framework.pojo.IComponent;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class ComponentScanCompleteEvent extends Event {
    private static final HandlerList HANDLER_LIST = new HandlerList();
    @Getter
    private final Collection<IComponent> components;

    public ComponentScanCompleteEvent(@NotNull Collection<IComponent> components) {
        this.components = components;
    }

    public static @NotNull HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
