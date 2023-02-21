package org.huanshi.mc.framework.exception;

import org.huanshi.mc.framework.engine.Component;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

public class CircularDependencyException extends RuntimeException {
    public CircularDependencyException(@NotNull List<Class<? extends Component>> classList) {
        super("类 " + classList + " 之间发生了循环依赖");
    }
}
