package org.huanshi.mc.framework;

import lombok.SneakyThrows;
import org.huanshi.mc.framework.annotation.Autowired;
import org.huanshi.mc.framework.pojo.IComponent;
import org.huanshi.mc.framework.pojo.Registrable;
import org.huanshi.mc.framework.utils.ReflectUtils;
import org.jetbrains.annotations.NotNull;

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
                setup(plugin, componentClass, new LinkedList<>(){{ add(componentClass); }});
            }
        }
    }

    @SuppressWarnings("unchecked")
    @SneakyThrows
    private static void setup(@NotNull AbstractPlugin plugin, @NotNull Class<? extends IComponent> clazz, @NotNull List<Class<? extends IComponent>> autowiredClasses) {
        if (!LOADED_COMPONENT_MAP.containsKey(clazz)) {
            IComponent component = AbstractPlugin.class.isAssignableFrom(clazz) ? plugin : clazz.getConstructor().newInstance();
            for (Field field : ReflectUtils.getFields(clazz)) {
                field.setAccessible(true);
                if (field.getAnnotation(Autowired.class) != null) {
                    Class<?> fieldClass = field.getType();
                    int modifiers = fieldClass.getModifiers();
                    if (IComponent.class.isAssignableFrom(fieldClass) && !Modifier.isAbstract(modifiers) && !Modifier.isInterface(modifiers)) {
                        for (Class<?> autowiredClass : autowiredClasses) {
                            if (autowiredClass.isAssignableFrom(fieldClass)) {
                                return;
                            }
                        }
                        Class<? extends IComponent> componentClass = (Class<? extends IComponent>) fieldClass;
                        autowiredClasses.add(componentClass);
                        setup(plugin, componentClass, autowiredClasses);
                        field.set(component, LOADED_COMPONENT_MAP.get(componentClass));
                        autowiredClasses.remove(fieldClass);
                    }
                }
            }
            component.superOnCreate(plugin);
            component.superOnLoad(plugin);
            if (Registrable.class.isAssignableFrom(clazz)) {
                ((Registrable) component).register(plugin);
            }
            LOADED_COMPONENT_MAP.put(clazz, component);
        }
    }
}
