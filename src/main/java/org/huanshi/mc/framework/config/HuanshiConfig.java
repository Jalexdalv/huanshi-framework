package org.huanshi.mc.framework.config;

import lombok.SneakyThrows;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public abstract non-sealed class HuanshiConfig extends AbstractConfig {
    @SneakyThrows
    public void save() {
        configuration.save(file);
    }

    public void set(@NotNull String path, @Nullable Object value) {
        configuration.set(path, value);
    }

    public @NotNull Location getLocation(@NotNull String path) {
        return Objects.requireNonNull(configuration.getLocation(path));
    }

    public @NotNull String getString(@NotNull String path) {
        return Objects.requireNonNull(configuration.getString(path));
    }

    public @NotNull List<String> getStringList(@NotNull String path) {
        return configuration.getStringList(path);
    }

    public @NotNull Set<String> getStringSet(@NotNull String path) {
        return new HashSet<>(configuration.getStringList(path));
    }

    public long getLong(@NotNull String path) {
        return configuration.getLong(path);
    }

    public int getInt(@NotNull String path) {
        return configuration.getInt(path);
    }

    public double getDouble(@NotNull String path) {
        return configuration.getDouble(path);
    }

    public float getFloat(@NotNull String path) {
        return (float) configuration.getDouble(path);
    }
}
