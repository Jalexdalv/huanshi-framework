package org.huanshi.mc.framework.engine;

import org.bukkit.Bukkit;
import org.huanshi.mc.framework.AbstractPlugin;
import org.huanshi.mc.framework.annotation.Autowired;
import org.huanshi.mc.framework.event.ComponentCreateEvent;
import org.huanshi.mc.framework.exception.CircularDependencyException;
import org.huanshi.mc.framework.utils.ReflectUtils;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ComponentFactory {
    private static final Map<Class<? extends Component>, Component> LOADED_COMPONENT_MAP = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static void create(@NotNull AbstractPlugin plugin) throws Throwable {
        for (Class<?> clazz : ReflectUtils.getJarClasses(plugin.getClass())) {
            if (Component.class.isAssignableFrom(clazz) && !Modifier.isAbstract(clazz.getModifiers())) {
                final Class<? extends Component> componentClass = (Class<? extends Component>) clazz;
                create(plugin, componentClass, new HashSet<>(){{ add(componentClass); }});
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static @NotNull Component create(@NotNull AbstractPlugin plugin, @NotNull Class<? extends Component> clazz, @NotNull Set<Class<? extends Component>> autowiredClassSet) throws Throwable {
        Component component = LOADED_COMPONENT_MAP.get(clazz);
        if (component == null) {
            component = clazz.getConstructor().newInstance();
            for (Field field : ReflectUtils.getFields(clazz)) {
                field.setAccessible(true);
                if (field.getAnnotation(Autowired.class) != null) {
                    final Class<?> fieldClass = field.getType();
                    if (AbstractPlugin.class.isAssignableFrom(fieldClass)) {
                        field.set(component, plugin);
                    } else if (Component.class.isAssignableFrom(fieldClass) && !Modifier.isAbstract(fieldClass.getModifiers())) {
                        for (Class<?> autowiredClass : autowiredClassSet) {
                            if (autowiredClass.isAssignableFrom(fieldClass)) {
                                throw new CircularDependencyException(autowiredClassSet);
                            }
                        }
                        final Class<? extends Component> componentClass = (Class<? extends Component>) fieldClass;
                        autowiredClassSet.add(componentClass);
                        field.set(component, create(plugin, componentClass, autowiredClassSet));
                        autowiredClassSet.remove(fieldClass);
                    }
                }
            }
            component.load();
            if (Registrable.class.isAssignableFrom(clazz)) {
                ((Registrable) component).register();
            }
            Bukkit.getPluginManager().callEvent(new ComponentCreateEvent<>(component));
            LOADED_COMPONENT_MAP.put(clazz, component);
        }
        return component;
    }
}
