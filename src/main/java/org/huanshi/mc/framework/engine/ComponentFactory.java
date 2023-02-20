package org.huanshi.mc.framework.engine;

import org.bukkit.Bukkit;
import org.huanshi.mc.framework.AbstractPlugin;
import org.huanshi.mc.framework.annotation.Autowired;
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

    public static void create(@NotNull AbstractPlugin plugin) throws Throwable {
        for (Class<?> clazz : ReflectUtils.getJarClasses(plugin.getClass())) {
            if (Component.class.isAssignableFrom(clazz) && !Modifier.isAbstract(clazz.getModifiers())) {
                create(plugin, (Class<? extends Component>) clazz, new HashSet<>(){{ add((Class<? extends Component>) clazz); }});
            }
        }
    }

    private static @NotNull Component create(@NotNull AbstractPlugin plugin, @NotNull Class<? extends Component> clazz, @NotNull Set<Class<? extends Component>> autowiredClassSet) throws Throwable {
        Component component = LOADED_COMPONENT_MAP.get(clazz);
        if (component == null) {
            component = clazz.getConstructor().newInstance();
            for (Field field : ReflectUtils.getFields(clazz)) {
                field.setAccessible(true);
                if (field.getAnnotation(Autowired.class) != null) {
                    Class<?> fieldClass = field.getType();
                    if (AbstractPlugin.class.isAssignableFrom(fieldClass)) {
                        field.set(component, plugin);
                    } else {
                        if (Component.class.isAssignableFrom(fieldClass) && !Modifier.isAbstract(fieldClass.getModifiers())) {
                            for (Class<?> autowiredClass : autowiredClassSet) {
                                if (autowiredClass.isAssignableFrom(fieldClass)) {
                                    throw new CircularDependencyException(autowiredClassSet);
                                }
                            }
                            autowiredClassSet.add((Class<? extends Component>) fieldClass);
                            field.set(component, create(plugin, (Class<? extends Component>) fieldClass, autowiredClassSet));
                            autowiredClassSet.remove(fieldClass);
                        }
                    }
                }
            }
            component.load();
            if (Registrable.class.isAssignableFrom(clazz)) {
                ((Registrable) component).register();
            }
            LOADED_COMPONENT_MAP.put(clazz, component);
            Bukkit.getLogger().info("[" + plugin.getName() + "] " + clazz.getName() + " 已加载");
        }
        return component;
    }
}
