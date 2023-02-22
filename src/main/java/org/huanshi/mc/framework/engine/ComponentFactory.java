package org.huanshi.mc.framework.engine;

import org.huanshi.mc.framework.AbstractPlugin;
import org.huanshi.mc.framework.annotation.Autowired;
import org.huanshi.mc.framework.api.BukkitApi;
import org.huanshi.mc.framework.event.ComponentScanCompleteEvent;
import org.huanshi.mc.framework.exception.CircularDependencyException;
import org.huanshi.mc.framework.utils.ReflectUtils;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ComponentFactory {
    private static final Map<Class<? extends Component>, Component> LOADED_COMPONENT_MAP = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static void scan(@NotNull AbstractPlugin plugin) throws Throwable {
        for (Class<?> clazz : ReflectUtils.getJarClasses(plugin.getClass())) {
            int modifiers = clazz.getModifiers();
            if (Component.class.isAssignableFrom(clazz) && !Modifier.isAbstract(modifiers) && !Modifier.isInterface(modifiers)) {
                Class<? extends Component> componentClass = (Class<? extends Component>) clazz;
                create(plugin, componentClass, new LinkedList<>(){{ add(componentClass); }});
            }
        }
        BukkitApi.callEvent(new ComponentScanCompleteEvent(LOADED_COMPONENT_MAP.values()));
    }

    @SuppressWarnings("unchecked")
    private static @NotNull Component create(@NotNull AbstractPlugin plugin, @NotNull Class<? extends Component> clazz, @NotNull List<Class<? extends Component>> autowiredClassList) throws Throwable {
        Component component = LOADED_COMPONENT_MAP.get(clazz);
        if (component == null) {
            component = clazz.getConstructor().newInstance();
            for (Field field : ReflectUtils.getFields(clazz)) {
                field.setAccessible(true);
                if (field.getAnnotation(Autowired.class) != null) {
                    Class<?> fieldClass = field.getType();
                    int modifiers = fieldClass.getModifiers();
                    if (AbstractPlugin.class.isAssignableFrom(fieldClass)) {
                        field.set(component, plugin);
                    } else if (Component.class.isAssignableFrom(fieldClass) && !Modifier.isAbstract(modifiers) && !Modifier.isInterface(modifiers)) {
                        for (Class<?> autowiredClass : autowiredClassList) {
                            if (autowiredClass.isAssignableFrom(fieldClass)) {
                                throw new CircularDependencyException(autowiredClassList);
                            }
                        }
                        Class<? extends Component> componentClass = (Class<? extends Component>) fieldClass;
                        autowiredClassList.add(componentClass);
                        field.set(component, create(plugin, componentClass, autowiredClassList));
                        autowiredClassList.remove(fieldClass);
                    }
                }
            }
            component.onCreate();
            component.onLoad();
            if (Registrable.class.isAssignableFrom(clazz)) {
                ((Registrable) component).register();
            }
            LOADED_COMPONENT_MAP.put(clazz, component);
        }
        return component;
    }
}
