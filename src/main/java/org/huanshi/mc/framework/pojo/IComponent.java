package org.huanshi.mc.framework.pojo;

import lombok.SneakyThrows;
import org.huanshi.mc.framework.AbstractPlugin;
import org.huanshi.mc.framework.utils.ReflectUtils;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;

public interface IComponent {
    void onCreate(@NotNull AbstractPlugin plugin) throws Throwable;
    void onLoad(@NotNull AbstractPlugin plugin)  throws Throwable;

    @SneakyThrows
    default void superOnCreate(@NotNull AbstractPlugin plugin) {
        for (Class<?> clazz : ReflectUtils.getClasses(getClass())) {
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.getName().equals("onCreate") && method.getParameters().length == 1 && AbstractPlugin.class.isAssignableFrom(method.getParameters()[0].getType())) {
                    ReflectUtils.getMethodHandle(method).invoke(this, plugin);
                }
            }
        }
    }

    @SneakyThrows
    default void superOnLoad(@NotNull AbstractPlugin plugin) {
        for (Class<?> clazz : ReflectUtils.getClasses(getClass())) {
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.getName().equals("onLoad") && method.getParameters().length == 1 && AbstractPlugin.class.isAssignableFrom(method.getParameters()[0].getType())) {
                    ReflectUtils.getMethodHandle(method).invoke(this, plugin);
                }
            }
        }
    }
}
