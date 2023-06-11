package org.huanshi.mc.framework;

import lombok.SneakyThrows;
import org.huanshi.mc.framework.annotation.HuanshiAutowired;
import org.huanshi.mc.framework.pojo.IHuanshiComponent;
import org.huanshi.mc.framework.pojo.IHuanshiRegistrar;
import org.huanshi.mc.framework.utils.ReflectUtils;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Bootstrap {
    private static final Map<Class<? extends IHuanshiComponent>, IHuanshiComponent> COMPONENT_MAP = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static void scan(@NotNull AbstractHuanshiPlugin plugin) {
        for (Class<?> cls : ReflectUtils.getJarClasses(plugin.getClass())) {
            int modifiers = cls.getModifiers();
            if (IHuanshiComponent.class.isAssignableFrom(cls) && !Modifier.isAbstract(modifiers) && !Modifier.isInterface(modifiers)) {
                Class<? extends IHuanshiComponent> componentClass = (Class<? extends IHuanshiComponent>) cls;
                init(plugin, componentClass, new LinkedList<>(){{ add(componentClass); }});
            }
        }
    }

    @SuppressWarnings("unchecked")
    @SneakyThrows
    private static void init(@NotNull AbstractHuanshiPlugin plugin, @NotNull Class<? extends IHuanshiComponent> cls, @NotNull List<Class<? extends IHuanshiComponent>> autowiredClasses) {
        if (COMPONENT_MAP.containsKey(cls)) {
            return;
        }
        IHuanshiComponent component = AbstractHuanshiPlugin.class.isAssignableFrom(cls) ? plugin : cls.getConstructor().newInstance();
        for (Field field : ReflectUtils.getFields(cls)) {
            field.setAccessible(true);
            if (field.getAnnotation(HuanshiAutowired.class) != null) {
                Class<?> fieldClass = field.getType();
                int modifiers = fieldClass.getModifiers();
                if (IHuanshiComponent.class.isAssignableFrom(fieldClass) && !Modifier.isAbstract(modifiers) && !Modifier.isInterface(modifiers)) {
                    for (Class<?> autowiredClass : autowiredClasses) {
                        if (autowiredClass.isAssignableFrom(fieldClass)) {
                            return;
                        }
                    }
                    Class<? extends IHuanshiComponent> componentClass = (Class<? extends IHuanshiComponent>) fieldClass;
                    autowiredClasses.add(componentClass);
                    init(plugin, componentClass, autowiredClasses);
                    autowiredClasses.remove(fieldClass);
                    field.set(component, COMPONENT_MAP.get(componentClass));
                }
            }
        }
        component.superOnCreate(plugin);
        component.superOnLoad(plugin);
        if (IHuanshiRegistrar.class.isAssignableFrom(cls)) {
            ((IHuanshiRegistrar) component).register(plugin);
        }
        COMPONENT_MAP.put(cls, component);
    }
}
