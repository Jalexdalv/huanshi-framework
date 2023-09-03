package org.huanshi.mc.framework.config;

import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.huanshi.mc.framework.plugin.AbstractPlugin;
import org.huanshi.mc.framework.annotation.Config;
import org.huanshi.mc.framework.pojo.IComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public abstract class AbstractConfig implements IComponent {
    private final String name = Objects.requireNonNull(StringUtils.trimToNull(getClass().getAnnotation(Config.class).file()));
    private File file;
    private YamlConfiguration configuration;

    @Override
    public void onCreate(@NotNull AbstractPlugin plugin) {
        file = new File(plugin.getDataFolder(), name);
    }

    @SneakyThrows
    @Override
    public void onLoad(@NotNull AbstractPlugin plugin) {
        try (InputStreamReader inputStreamReader = new InputStreamReader(Objects.requireNonNull(plugin.getResource(name)))) {
            if (file.exists()) {
                configuration = YamlConfiguration.loadConfiguration(file);
                configuration.setDefaults(YamlConfiguration.loadConfiguration(inputStreamReader));
                configuration.options().copyDefaults(true);
            } else {
                configuration = YamlConfiguration.loadConfiguration(inputStreamReader);
            }
            save();
        }
    }

    @SneakyThrows
    public void save() {
        configuration.save(file);
    }

    public void set(@NotNull String key, @Nullable Object value) {
        configuration.set(key, value);
    }

    public @NotNull Location getLocation(@NotNull String key) {
        return Objects.requireNonNull(configuration.getLocation(key));
    }

    public @NotNull String getString(@NotNull String key) {
        return Objects.requireNonNull(configuration.getString(key));
    }

    public @NotNull List<String> getStringList(@NotNull String key) {
        return configuration.getStringList(key);
    }

    public @NotNull Set<String> getStringSet(@NotNull String key) {
        return new HashSet<>(configuration.getStringList(key));
    }

    public long getLong(@NotNull String key) {
        return configuration.getLong(key);
    }

    public int getInt(@NotNull String key) {
        return configuration.getInt(key);
    }

    public double getDouble(@NotNull String key) {
        return configuration.getDouble(key);
    }

    public float getFloat(@NotNull String key) {
        return (float) configuration.getDouble(key);
    }
}
