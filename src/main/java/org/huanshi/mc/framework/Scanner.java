package org.huanshi.mc.framework;

import com.google.common.collect.ImmutableList;
import lombok.SneakyThrows;
import org.huanshi.mc.framework.annotation.Autowired;
import org.huanshi.mc.framework.api.BukkitAPI;
import org.huanshi.mc.framework.event.ComponentScanCompleteEvent;
import org.huanshi.mc.framework.pojo.IComponent;
import org.huanshi.mc.framework.pojo.Registrable;
import org.huanshi.mc.framework.utils.ReflectUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Scanner {
    private static final Map<Class<? extends IComponent>, IComponent> LOADED_COMPONENT_MAP = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static void scan(@NotNull AbstractPlugin plugin) {
        for (Class<?> clazz : ReflectUtils.getJarClasses(plugin.getClass())) {
            int modifiers = clazz.getModifiers();
            if (IComponent.class.isAssignableFrom(clazz) && !Modifier.isAbstract(modifiers) && !Modifier.isInterface(modifiers)) {
                Class<? extends IComponent> componentClass = (Class<? extends IComponent>) clazz;
                LOADED_COMPONENT_MAP.put(componentClass, setup(plugin, componentClass, new LinkedList<>(){{ add(componentClass); }}));
            }
        }
        BukkitAPI.callEvent(new ComponentScanCompleteEvent(ImmutableList.copyOf(LOADED_COMPONENT_MAP.values())));
    }

    @SuppressWarnings("unchecked")
    @SneakyThrows
    private static @Nullable IComponent setup(@NotNull AbstractPlugin plugin, @NotNull Class<? extends IComponent> clazz, @NotNull List<Class<? extends IComponent>> autowiredClasses) {
        IComponent component = LOADED_COMPONENT_MAP.get(clazz);
        if (component == null) {
            component = AbstractPlugin.class.isAssignableFrom(clazz) ? plugin : clazz.getConstructor().newInstance();
            for (Field field : ReflectUtils.getFields(clazz)) {
                field.setAccessible(true);
                if (field.getAnnotation(Autowired.class) != null) {
                    Class<?> fieldClass = field.getType();
                    int modifiers = fieldClass.getModifiers();
                    if (IComponent.class.isAssignableFrom(fieldClass) && !Modifier.isAbstract(modifiers) && !Modifier.isInterface(modifiers)) {
                        for (Class<?> autowiredClass : autowiredClasses) {
                            if (autowiredClass.isAssignableFrom(fieldClass)) {
                                return null;
                            }
                        }
                        Class<? extends IComponent> componentClass = (Class<? extends IComponent>) fieldClass;
                        autowiredClasses.add(componentClass);
                        field.set(component, setup(plugin, componentClass, autowiredClasses));
                        autowiredClasses.remove(fieldClass);
                    }
                }
            }
            component.onCreate(plugin);
            component.onLoad(plugin);
            if (Registrable.class.isAssignableFrom(clazz)) {
                ((Registrable) component).register(plugin);
            }
        }
        return component;
    }
}
