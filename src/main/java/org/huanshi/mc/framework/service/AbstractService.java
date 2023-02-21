package org.huanshi.mc.framework.service;

import org.huanshi.mc.framework.engine.Component;

public abstract class AbstractService implements Component {
    @Override
    public void onCreate() {}

    @Override
    public void onLoad() {}
}
