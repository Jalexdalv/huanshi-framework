package org.huanshi.mc.framework.protocol;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import lombok.SneakyThrows;
import org.huanshi.mc.framework.AbstractPlugin;
import org.huanshi.mc.framework.pojo.HuanshiComponent;
import org.huanshi.mc.framework.pojo.Registrable;
import org.huanshi.mc.framework.annotation.ProtocolHandler;
import org.huanshi.mc.framework.utils.ReflectUtils;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class AbstractProtocol implements HuanshiComponent, Registrable {
    private static ProtocolManager protocolManager;

    @Override
    public void create(@NotNull AbstractPlugin plugin) {
        if (protocolManager == null) {
            protocolManager = ProtocolLibrary.getProtocolManager();
        }
    }

    @Override
    public void load(@NotNull AbstractPlugin plugin) {}

    @SneakyThrows
    @Override
    public void register(@NotNull AbstractPlugin plugin) {
        for (Method method : ReflectUtils.getMethods(getClass())) {
            if (method.getAnnotation(ProtocolHandler.class) != null && method.getReturnType() == PacketAdapter.class) {
                getProtocolManager().addPacketListener((PacketAdapter) method.invoke(this));
            }
        }
    }

    protected @NotNull ProtocolManager getProtocolManager() {
        return protocolManager;
    }
}
