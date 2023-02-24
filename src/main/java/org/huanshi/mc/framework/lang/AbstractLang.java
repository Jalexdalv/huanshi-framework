package org.huanshi.mc.framework.lang;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import org.huanshi.mc.framework.AbstractPlugin;
import org.huanshi.mc.framework.config.AbstractConfig;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

public abstract class AbstractLang extends AbstractConfig {
    private static MiniMessage miniMessage;

    @Override
    public void onCreate(@NotNull AbstractPlugin plugin) {
        super.onCreate(plugin);
        if (miniMessage == null) {
            miniMessage = MiniMessage.miniMessage();
        }
    }

    public @NotNull Component getComponent(@NotNull String path) {
        return miniMessage.deserialize(getString(path));
    }

    public @NotNull Title getTitle(@NotNull Component titleComponent, @NotNull Component subTitleComponent, long fideIn, long stay, long fideOut) {
        return Title.title(titleComponent, subTitleComponent, Title.Times.times(Duration.ofMillis(fideIn), Duration.ofMillis(stay), Duration.ofMillis(fideOut)));
    }

    public @NotNull Component format(@NotNull Component component, @NotNull Object @NotNull ... values) {
        for (int i = 0, len = values.length; i < len; i++) {
            component = component.replaceText(TextReplacementConfig.builder().matchLiteral("{" + i + "}").replacement(values[i].toString()).build());
        }
        return component;
    }
}
