package org.huanshi.mc.framework.config;

import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.configuration.file.YamlConfiguration;
import org.huanshi.mc.framework.HuanshiPlugin;
import org.huanshi.mc.framework.pojo.HuanshiComponent;
import org.huanshi.mc.framework.annotation.Config;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.InputStreamReader;
import java.util.Objects;

public abstract sealed class AbstractConfig implements HuanshiComponent permits HuanshiConfig {
    protected final @NotNull String fileName = StringUtils.trimToNull(getClass().getAnnotation(Config.class).file());
    protected File file;
    protected YamlConfiguration configuration;

    @Override
    public void onCreate(@NotNull HuanshiPlugin plugin) {
        file = new File(plugin.getDataFolder(), fileName);
    }

    @SneakyThrows
    @Override
    public void onLoad(@NotNull HuanshiPlugin plugin) {
        try (InputStreamReader inputStreamReader = new InputStreamReader(Objects.requireNonNull(plugin.getResource(fileName)))) {
            if (file.exists()) {
                configuration = YamlConfiguration.loadConfiguration(file);
                configuration.setDefaults(YamlConfiguration.loadConfiguration(inputStreamReader));
                configuration.options().copyDefaults(true);
            } else {
                configuration = YamlConfiguration.loadConfiguration(inputStreamReader);
            }
            configuration.save(file);
        }
    }
}
