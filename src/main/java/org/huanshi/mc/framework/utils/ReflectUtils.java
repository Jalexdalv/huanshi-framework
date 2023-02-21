package org.huanshi.mc.framework.utils;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ReflectUtils {
    public static @NotNull List<Method> getMethods(@NotNull Class<?> clazz) {
        List<Method> methodList = new LinkedList<>();
        while (clazz != null) {
            methodList.addAll(0, Arrays.asList(clazz.getDeclaredMethods()));
            clazz = clazz.getSuperclass();
        }
        return methodList;
    }

    public static @NotNull List<Field> getFields(@NotNull Class<?> clazz) {
        List<Field> fieldList = new LinkedList<>();
        while (clazz != null) {
            fieldList.addAll(0, Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        return fieldList;
    }

    public static @NotNull List<Class<?>> getJarClasses(@NotNull Class<?> clazz) throws IOException, ClassNotFoundException {
        List<Class<?>> classList = new LinkedList<>();
        try (JarFile jarFile = new JarFile(clazz.getProtectionDomain().getCodeSource().getLocation().getFile())) {
            Enumeration<JarEntry> jarEntryEnumeration = jarFile.entries();
            while (jarEntryEnumeration.hasMoreElements()) {
                String name = jarEntryEnumeration.nextElement().getName();
                if (StringUtils.endsWith(name, ".class") && !StringUtils.contains(name, "$")) {
                    classList.add(Class.forName(StringUtils.replace(name, "/", ".").replaceAll(".class", "")));
                }
            }
        }
        return classList;
    }
}
