package org.huanshi.mc.framework.mapper;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.SneakyThrows;
import org.huanshi.mc.framework.plugin.AbstractPlugin;
import org.huanshi.mc.framework.pojo.IComponent;
import org.huanshi.mc.framework.annotation.Autowired;
import org.huanshi.mc.framework.config.MainConfig;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;

public abstract class AbstractMapper implements IComponent {
    private static HikariDataSource hikariDataSource;
    @Autowired
    private MainConfig mainConfig;

    @Override
    public void onCreate(@NotNull AbstractPlugin plugin) {}

    @Override
    public void onLoad(@NotNull AbstractPlugin plugin) {
        if (hikariDataSource == null) {
            HikariConfig hikariConfig = new HikariConfig();
            hikariConfig.setConnectionTimeout(mainConfig.getLong("data-source.mysql.connection-timeout"));
            hikariConfig.setMinimumIdle(mainConfig.getInt("data-source.mysql.minimum-idle"));
            hikariConfig.setMaximumPoolSize(mainConfig.getInt("data-source.mysql.maximum-pool-size"));
            hikariConfig.setJdbcUrl("jdbc:mysql://" + mainConfig.getString("data-source.mysql.address") + ":" + mainConfig.getInt("data-source.mysql.port") + "/" + mainConfig.getString("data-source.mysql.database") + "?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=Asia/Shanghai");
            hikariConfig.setUsername(mainConfig.getString("data-source.mysql.user"));
            hikariConfig.setPassword(mainConfig.getString("data-source.mysql.password"));
            hikariConfig.setAutoCommit(true);
            hikariDataSource = new HikariDataSource(hikariConfig);
        }
    }

    @SneakyThrows
    protected @NotNull Connection getMySQLConnection() {
        return hikariDataSource.getConnection();
    }
}
