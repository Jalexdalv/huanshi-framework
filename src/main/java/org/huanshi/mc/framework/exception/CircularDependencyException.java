package org.huanshi.mc.framework.exception;

import org.huanshi.mc.framework.pojo.HuanshiComponent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CircularDependencyException extends RuntimeException {
    public CircularDependencyException(@NotNull List<Class<? extends HuanshiComponent>> huanshiComponentList) {
        super("类 " + huanshiComponentList + " 之间发生了循环依赖");
    }
}
