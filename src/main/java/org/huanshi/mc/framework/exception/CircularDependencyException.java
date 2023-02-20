package org.huanshi.mc.framework.exception;

import org.huanshi.mc.framework.engine.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class CircularDependencyException extends RuntimeException {
    public CircularDependencyException(@NotNull final Set<Class<? extends Component>> classSet) {
        super("类 " + classSet + " 之间发生了循环依赖");
    }
}
