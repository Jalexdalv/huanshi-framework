package org.huanshi.mc.framework;

import lombok.SneakyThrows;
import org.huanshi.mc.framework.AbstractPlugin;
import org.huanshi.mc.framework.annotation.Autowired;
import org.huanshi.mc.framework.api.BukkitAPI;
import org.huanshi.mc.framework.event.ComponentScanCompleteEvent;
import org.huanshi.mc.framework.pojo.HuanshiComponent;
import org.huanshi.mc.framework.pojo.Registrable;
import org.huanshi.mc.framework.utils.ReflectUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Scanner {
    private static final Map<Class<? extends HuanshiComponent>, HuanshiComponent> LOADED_HUANSHI_COMPONENT_MAP = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static void scan(@NotNull AbstractPlugin plugin) {
        for (Class<?> clazz : ReflectUtils.getJarClasses(plugin.getClass())) {
            int modifiers = clazz.getModifiers();
            if (HuanshiComponent.class.isAssignableFrom(clazz) && !Modifier.isAbstract(modifiers) && !Modifier.isInterface(modifiers)) {
                Class<? extends HuanshiComponent> huanshiComponentClass = (Class<? extends HuanshiComponent>) clazz;
                LOADED_HUANSHI_COMPONENT_MAP.put(huanshiComponentClass, setup(plugin, huanshiComponentClass, new LinkedList<>(){{ add(huanshiComponentClass); }}));
            }
        }
        BukkitAPI.callEvent(new ComponentScanCompleteEvent(Collections.unmodifiableCollection(LOADED_HUANSHI_COMPONENT_MAP.values())));
    }

    @SuppressWarnings("unchecked")
    @SneakyThrows
    private static @Nullable HuanshiComponent setup(@NotNull AbstractPlugin plugin, @NotNull Class<? extends HuanshiComponent> clazz, @NotNull List<Class<? extends HuanshiComponent>> autowiredClasses) {
        HuanshiComponent huanshiComponent = LOADED_HUANSHI_COMPONENT_MAP.get(clazz);
        if (huanshiComponent == null) {
            huanshiComponent = AbstractPlugin.class.isAssignableFrom(clazz) ? plugin : clazz.getConstructor().newInstance();
            for (Field field : ReflectUtils.getFields(clazz)) {
                field.setAccessible(true);
                if (field.getAnnotation(Autowired.class) != null) {
                    Class<?> fieldClass = field.getType();
                    int modifiers = fieldClass.getModifiers();
                    if (HuanshiComponent.class.isAssignableFrom(fieldClass) && !Modifier.isAbstract(modifiers) && !Modifier.isInterface(modifiers)) {
                        for (Class<?> autowiredClass : autowiredClasses) {
                            if (autowiredClass.isAssignableFrom(fieldClass)) {
                                return null;
                            }
                        }
                        Class<? extends HuanshiComponent> componentClass = (Class<? extends HuanshiComponent>) fieldClass;
                        autowiredClasses.add(componentClass);
                        field.set(huanshiComponent, setup(plugin, componentClass, autowiredClasses));
                        autowiredClasses.remove(fieldClass);
                    }
                }
            }
            huanshiComponent.onCreate(plugin);
            huanshiComponent.onLoad(plugin);
            if (Registrable.class.isAssignableFrom(clazz)) {
                ((Registrable) huanshiComponent).register(plugin);
            }
        }
        return huanshiComponent;
    }
}