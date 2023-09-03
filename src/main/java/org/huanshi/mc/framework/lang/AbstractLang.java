package org.huanshi.mc.framework.lang;

import lombok.SneakyThrows;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.configuration.file.YamlConfiguration;
import org.huanshi.mc.framework.annotation.Lang;
import org.huanshi.mc.framework.plugin.AbstractPlugin;
import org.huanshi.mc.framework.pojo.IComponent;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.Objects;

public abstract class AbstractLang implements IComponent {
    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    private final String name = Objects.requireNonNull(StringUtils.trimToNull(getClass().getAnnotation(Lang.class).file()));
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

    public @NotNull Component getComponent(@NotNull String key) {
        return MINI_MESSAGE.deserialize(Objects.requireNonNull(configuration.getString(key)));
    }

    public @NotNull Title getTitle(@NotNull Component titleComponent, @NotNull Component subTitleComponent, long fideIn, long stay, long fideOut) {
        return Title.title(titleComponent, subTitleComponent, Title.Times.times(Duration.ofMillis(fideIn), Duration.ofMillis(stay), Duration.ofMillis(fideOut)));
    }

    public @NotNull Component format(@NotNull Component component, @NotNull Object @NotNull ... values) {
        for (int index = 0, len = values.length; index < len; index++) {
            component = component.replaceText(TextReplacementConfig.builder().matchLiteral("{" + index + "}").replacement(values[index].toString()).build());
        }
        return component;
    }
}
