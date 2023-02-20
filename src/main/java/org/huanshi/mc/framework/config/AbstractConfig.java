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
    public final void load() throws IOException {
        final Config config = getClass().getAnnotation(Config.class);
        file = new File(plugin.getDataFolder(), config.file());
        try (final InputStreamReader inputStreamReader = new InputStreamReader(Objects.requireNonNull(plugin.getResource(config.file())))) {
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

    public final void save() throws IOException {
        configuration.save(file);
    }

    public final void set(@NotNull final String path, @Nullable final Object value) {
        configuration.set(path, value);
    }

    public final @NotNull Location getLocation(@NotNull final String path) {
        return Objects.requireNonNull(configuration.getLocation(path));
    }

    public final @NotNull String getString(@NotNull final String path) {
        return Objects.requireNonNull(configuration.getString(path));
    }

    public final @NotNull List<String> getStringList(@NotNull final String path) {
        return configuration.getStringList(path);
    }

    public final @NotNull Set<String> getStringSet(@NotNull final String path) {
        return new HashSet<>(configuration.getStringList(path));
    }

    public final long getLong(@NotNull final String path) {
        return configuration.getLong(path);
    }

    public final int getInt(@NotNull final String path) {
        return configuration.getInt(path);
    }

    public final double getDouble(@NotNull final String path) {
        return configuration.getDouble(path);
    }

    public final float getFloat(@NotNull final String path) {
        return (float) configuration.getDouble(path);
    }
}
