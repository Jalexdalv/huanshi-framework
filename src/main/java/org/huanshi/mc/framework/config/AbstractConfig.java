package org.huanshi.mc.framework.config;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.huanshi.mc.framework.AbstractPlugin;
import org.huanshi.mc.framework.engine.Component;
import org.huanshi.mc.framework.annotation.Autowired;
import org.huanshi.mc.framework.annotation.Config;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public abstract class AbstractConfig implements Component {
    @Autowired
    private AbstractPlugin plugin;
    protected File file;
    protected YamlConfiguration configuration;

    @Override
    public void onCreate() throws IOException {
        Config config = getClass().getAnnotation(Config.class);
        file = new File(plugin.getDataFolder(), config.file());
        try (InputStreamReader inputStreamReader = new InputStreamReader(Objects.requireNonNull(plugin.getResource(config.file())))) {
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

    @Override
    public void onLoad() {}

    public final void save() throws IOException {
        configuration.save(file);
    }

    public final void set(@NotNull String path, @Nullable Object value) {
        configuration.set(path, value);
    }

    public final @NotNull Location getLocation(@NotNull String path) {
        return Objects.requireNonNull(configuration.getLocation(path));
    }

    public final @NotNull String getString(@NotNull String path) {
        return Objects.requireNonNull(configuration.getString(path));
    }

    public final @NotNull List<String> getStringList(@NotNull String path) {
        return configuration.getStringList(path);
    }

    public final @NotNull Set<String> getStringSet(@NotNull String path) {
        return new HashSet<>(configuration.getStringList(path));
    }

    public final long getLong(@NotNull String path) {
        return configuration.getLong(path);
    }

    public final int getInt(@NotNull String path) {
        return configuration.getInt(path);
    }

    public final double getDouble(@NotNull String path) {
        return configuration.getDouble(path);
    }

    public final float getFloat(@NotNull String path) {
        return (float) configuration.getDouble(path);
    }
}
