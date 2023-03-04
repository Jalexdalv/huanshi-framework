package org.huanshi.mc.framework;

import lombok.SneakyThrows;
import org.huanshi.mc.framework.annotation.Autowired;
import org.huanshi.mc.framework.pojo.IComponent;
import org.huanshi.mc.framework.pojo.Registrar;
import org.huanshi.mc.framework.utils.ReflectUtils;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Bootstrap {
    private static final Map<Class<? extends IComponent>, IComponent> COMPONENT_MAP = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static void scan(@NotNull AbstractPlugin plugin) {
        for (Class<?> cls : ReflectUtils.getJarClasses(plugin.getClass())) {
            int modifiers = cls.getModifiers();
            if (IComponent.class.isAssignableFrom(cls) && !Modifier.isAbstract(modifiers) && !Modifier.isInterface(modifiers)) {
                Class<? extends IComponent> componentClass = (Class<? extends IComponent>) cls;
                init(plugin, componentClass, new LinkedList<>(){{ add(componentClass); }});
            }
        }
    }

    @SuppressWarnings("unchecked")
    @SneakyThrows
    private static void init(@NotNull AbstractPlugin plugin, @NotNull Class<? extends IComponent> cls, @NotNull List<Class<? extends IComponent>> autowiredClasses) {
        if (COMPONENT_MAP.containsKey(cls)) {
            return;
        }
        IComponent component = AbstractPlugin.class.isAssignableFrom(cls) ? plugin : cls.getConstructor().newInstance();
        for (Field field : ReflectUtils.getFields(cls)) {
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
                    init(plugin, componentClass, autowiredClasses);
                    autowiredClasses.remove(fieldClass);
                    field.set(component, COMPONENT_MAP.get(componentClass));
                }
            }
        }
        component.superOnCreate(plugin);
        component.superOnLoad(plugin);
        if (Registrar.class.isAssignableFrom(cls)) {
            ((Registrar) component).register(plugin);
        }
        COMPONENT_MAP.put(cls, component);
    }
}
