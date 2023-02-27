package org.huanshi.mc.framework.utils;

import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ReflectUtils {
    public static @NotNull List<Class<?>> getClasses(@NotNull Class<?> clazz) {
        LinkedList<Class<?>> classList = new LinkedList<>();
        while (clazz != null) {
            classList.addFirst(clazz);
            clazz = clazz.getSuperclass();
        }
        return classList;
    }

    public static @NotNull List<Method> getMethods(@NotNull Class<?> clazz) {
        List<Method> methods = new LinkedList<>();
        while (clazz != null) {
            methods.addAll(0, Arrays.asList(clazz.getDeclaredMethods()));
            clazz = clazz.getSuperclass();
        }
        return methods;
    }

    public static @NotNull List<Field> getFields(@NotNull Class<?> clazz) {
        List<Field> fields = new LinkedList<>();
        while (clazz != null) {
            fields.addAll(0, Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        return fields;
    }

    @SneakyThrows
    public static @NotNull List<Class<?>> getJarClasses(@NotNull Class<?> clazz) {
        List<Class<?>> classes = new LinkedList<>();
        try (JarFile jarFile = new JarFile(clazz.getProtectionDomain().getCodeSource().getLocation().getFile())) {
            Enumeration<JarEntry> jarEntryEnumeration = jarFile.entries();
            while (jarEntryEnumeration.hasMoreElements()) {
                String name = jarEntryEnumeration.nextElement().getName();
                if (StringUtils.endsWith(name, ".class") && !StringUtils.contains(name, "$")) {
                    classes.add(Class.forName(StringUtils.replace(name, "/", ".").replaceAll(".class", "")));
                }
            }
        }
        return classes;
    }

    @SneakyThrows
    public static @NotNull MethodHandle getMethodHandle(@NotNull Method method) {
        return ((MethodHandles.Lookup) MethodHandles.class.getMethod("privateLookupIn", Class.class, MethodHandles.Lookup.class).invoke(MethodHandles.class, method.getDeclaringClass(), MethodHandles.lookup())).unreflectSpecial(method, method.getDeclaringClass());
    }
}
