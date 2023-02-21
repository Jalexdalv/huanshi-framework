package org.huanshi.mc.framework.lang;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.huanshi.mc.framework.utils.FormatUtils;
import org.jetbrains.annotations.NotNull;

public class Zh {
    public static final Component ONLY_CONSOLE = Component.text("该指令只能在后台执行", NamedTextColor.RED);
    public static final Component ONLY_GAME = Component.text("该指令只能在游戏内执行", NamedTextColor.RED);
    public static final Component NO_PERMISSION = Component.text("你没有权限", NamedTextColor.RED);
    public static final Component CANNOT_USE_COMMAND = Component.text("当前无法使用该指令", NamedTextColor.RED);
    public static final Component PLAYER_NOT_FOUND = Component.text("该玩家不在线或不存在", NamedTextColor.RED);
    public static final Component WORLD_NOT_FOUND = Component.text("该世界不存在", NamedTextColor.RED);

    private static final Component PLUGIN_NAME_1 = Component.text("[");
    private static final Component PLUGIN_NAME_2 = Component.text("] ");
    private static final Component ENABLE = Component.text("插件已加载", NamedTextColor.GREEN);
    public static @NotNull Component enable(@NotNull String name) {
        return PLUGIN_NAME_1.append(Component.text(name)).append(PLUGIN_NAME_2).append(ENABLE);
    }
    private static final Component DISABLE = Component.text("插件已卸载", NamedTextColor.GREEN);
    public static @NotNull Component disable(@NotNull String name) {
        return PLUGIN_NAME_1.append(Component.text(name)).append(PLUGIN_NAME_2).append(DISABLE);
    }
    private static final Component CD_1 = Component.text("冷却中, 请等待 ", NamedTextColor.RED);
    private static final Component CD_2 = Component.text(" 秒", NamedTextColor.RED);
    public static @NotNull Component cd(long duration) {
        return CD_1.append(Component.text(FormatUtils.convertMillisecondToSecond(duration), NamedTextColor.YELLOW)).append(CD_2);
    }
}
