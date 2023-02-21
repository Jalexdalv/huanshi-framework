package org.huanshi.mc.framework.listener;

import org.huanshi.mc.framework.AbstractPlugin;
import org.huanshi.mc.framework.api.BukkitApi;
import org.huanshi.mc.framework.engine.Component;
import org.huanshi.mc.framework.engine.Registrable;
import org.huanshi.mc.framework.annotation.Autowired;

public abstract class AbstractListener implements Component, Registrable, org.bukkit.event.Listener {
    @Autowired
    private AbstractPlugin plugin;

    @Override
    public void onCreate() {}

    @Override
    public void onLoad() {}

    @Override
    public final void register() {
        BukkitApi.registerEvent(plugin, this);
    }
}
