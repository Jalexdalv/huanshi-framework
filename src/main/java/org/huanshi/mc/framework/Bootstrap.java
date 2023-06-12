package org.huanshi.mc.framework;

import lombok.SneakyThrows;
import org.huanshi.mc.framework.annotation.Autowired;
import org.huanshi.mc.framework.plugin.AbstractPlugin;
import org.huanshi.mc.framework.pojo.IComponent;
import org.huanshi.mc.framework.pojo.IRegistrar;
import org.huanshi.mc.framework.utils.ReflectUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Bootstrap {
    private static final Map<Class<? extends IComponent>, IComponent> COMPONENT_MAP = new HashMap<>();

    public static void scan(@NotNull AbstractPlugin plugin) {
        for (Class<?> cls : ReflectUtils.getJarClasses(plugin.getClass())) {
            init(plugin, cls, null);
        }
    }

    @SuppressWarnings("unchecked")
    @SneakyThrows
    private static void init(@NotNull AbstractPlugin plugin, @NotNull Class<?> cls, @Nullable List<Class<? extends IComponent>> autowiredClasses) {
        if (COMPONENT_MAP.containsKey(cls)) {
            return;
        }
        int modifiers = cls.getModifiers();
        if (Modifier.isAbstract(modifiers) || Modifier.isInterface(modifiers) || !IComponent.class.isAssignableFrom(cls)) {
            return;
        }
        if (autowiredClasses == null) {
            autowiredClasses = new LinkedList<>();
        } else {
            for (Class<? extends IComponent> autowiredClass : autowiredClasses) {
                assert !autowiredClass.isAssignableFrom(cls);
            }
        }
        Class<? extends IComponent> componentClass = (Class<? extends IComponent>) cls;
        autowiredClasses.add(componentClass);
        IComponent component = componentClass == plugin.getClass() ? plugin : componentClass.getConstructor().newInstance();
        for (Field field : ReflectUtils.getFields(componentClass)) {
            field.setAccessible(true);
            if (field.getAnnotation(Autowired.class) == null) {
                continue;
            }
            Class<?> fieldClass = field.getType();
            init(plugin, fieldClass, autowiredClasses);
            field.set(component, COMPONENT_MAP.get(fieldClass));
        }
        component.superOnCreate(plugin);
        component.superOnLoad(plugin);
        if (IRegistrar.class.isAssignableFrom(componentClass)) {
            ((IRegistrar) component).register(plugin);
        }
        COMPONENT_MAP.put(componentClass, component);
        autowiredClasses.remove(componentClass);
    }
}
