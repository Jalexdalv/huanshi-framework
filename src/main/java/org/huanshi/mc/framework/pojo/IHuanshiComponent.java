package org.huanshi.mc.framework.pojo;

import lombok.SneakyThrows;
import org.huanshi.mc.framework.AbstractHuanshiPlugin;
import org.huanshi.mc.framework.utils.ReflectUtils;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;

public interface IHuanshiComponent {
    void onCreate(@NotNull AbstractHuanshiPlugin huanshiPlugin) throws Throwable;
    void onLoad(@NotNull AbstractHuanshiPlugin huanshiPlugin)  throws Throwable;

    @SneakyThrows
    default void superOnCreate(@NotNull AbstractHuanshiPlugin huanshiPlugin) {
        for (Class<?> clazz : ReflectUtils.getClasses(getClass())) {
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.getName().equals("onCreate") && method.getParameters().length == 1 && AbstractHuanshiPlugin.class.isAssignableFrom(method.getParameters()[0].getType())) {
                    ReflectUtils.getMethodHandle(method).invoke(this, huanshiPlugin);
                }
            }
        }
    }

    @SneakyThrows
    default void superOnLoad(@NotNull AbstractHuanshiPlugin huanshiPlugin) {
        for (Class<?> clazz : ReflectUtils.getClasses(getClass())) {
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.getName().equals("onLoad") && method.getParameters().length == 1 && AbstractHuanshiPlugin.class.isAssignableFrom(method.getParameters()[0].getType())) {
                    ReflectUtils.getMethodHandle(method).invoke(this, huanshiPlugin);
                }
            }
        }
    }
}
