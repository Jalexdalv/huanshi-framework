package org.huanshi.mc.framework.lang;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import org.huanshi.mc.framework.AbstractPlugin;
import org.huanshi.mc.framework.config.AbstractConfig;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.text.MessageFormat;
import java.time.Duration;

public abstract class AbstractLang extends AbstractConfig {
    private static MiniMessage miniMessage;

    @Override
    public void create(@NotNull AbstractPlugin plugin) {
        file = new File(plugin.getDataFolder(), "lang/" + fileName);
        if (miniMessage == null) {
            miniMessage = MiniMessage.miniMessage();
        }
    }

    public @NotNull Component getComponent(@NotNull String path) {
        return miniMessage.deserialize(getString(path));
    }

    public @NotNull Component getComponent(@NotNull String path, @NotNull Object @NotNull ... values) {
        return miniMessage.deserialize(MessageFormat.format(getString(path), values));
    }

    public @NotNull Title getTitle(@NotNull Component title, @NotNull Component subTitle, long fideIn, long stay, long fideOut) {
        return Title.title(title, subTitle, Title.Times.times(Duration.ofMillis(fideIn), Duration.ofMillis(stay), Duration.ofMillis(fideOut)));
    }
}
