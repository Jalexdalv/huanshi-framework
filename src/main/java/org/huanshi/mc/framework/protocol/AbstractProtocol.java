package org.huanshi.mc.framework.protocol;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import lombok.SneakyThrows;
import org.huanshi.mc.framework.AbstractPlugin;
import org.huanshi.mc.framework.pojo.IComponent;
import org.huanshi.mc.framework.pojo.Registrar;
import org.huanshi.mc.framework.annotation.ProtocolHandler;
import org.huanshi.mc.framework.utils.ReflectUtils;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;

public abstract class AbstractProtocol implements IComponent, Registrar {
    protected static final ProtocolManager PROTOCOL_MANAGER = ProtocolLibrary.getProtocolManager();

    @Override
    public void onCreate(@NotNull AbstractPlugin huanshiPlugin) {}

    @Override
    public void onLoad(@NotNull AbstractPlugin huanshiPlugin) {}

    @SneakyThrows
    @Override
    public void register(@NotNull AbstractPlugin huanshiPlugin) {
        for (Method method : ReflectUtils.getMethods(getClass())) {
            if (method.getAnnotation(ProtocolHandler.class) != null && method.getReturnType() == PacketAdapter.class) {
                PROTOCOL_MANAGER.addPacketListener((PacketAdapter) method.invoke(this));
            }
        }
    }
}
