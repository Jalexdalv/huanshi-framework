package org.huanshi.mc.framework.helper;

import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.huanshi.mc.framework.AbstractHuanshiPlugin;
import org.huanshi.mc.framework.pojo.CoordinateType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ParticleHelper extends LocationHelper {
    private final Particle particle;
    private final int count;
    private final double offsetX, offsetY, offsetZ, speed;
    private final Object data;

    public ParticleHelper(@NotNull Particle particle, @NotNull Location location, int count, double offsetX, double offsetY, double offsetZ, double speed, @Nullable Object data) {
        super(location);
        this.particle = particle;
        this.count = count;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
        this.speed = speed;
        this.data = data;
    }

    public void play(@NotNull Player player) {
        player.spawnParticle(particle, this, count, correctX(offsetX, offsetZ), offsetY, correctZ(offsetX, offsetZ), speed, data);
    }

    public void play(double x1, double y1, double z1, double x2, double y2, double z2) {
        getNearbyEntity(Player.class, x1, y1, z1, x2, y2, z2, null, this::play);
    }

    public void play2D(@NotNull CoordinateType coordinateType, double startAngle, double endAngle, double radius, int repeat, double x1, double y1, double z1, double x2, double y2, double z2) {
        List<Player> players = getNearbyEntity(Player.class, x1, y1, z1, x2, y2, z2, null, null);
        double stepAngle = (endAngle - startAngle) / (double) repeat;
        switch (coordinateType) {
            case XY -> {
                for (double angle = startAngle; angle < endAngle; angle = angle + stepAngle) {
                    double radians = Math.toRadians(angle), x = radius * Math.sin(radians), y = radius * Math.sin(radians), correctX = correctX(x, 0), correctZ = correctZ(x, 0);
                    add(correctX, y, correctZ);
                    for (Player player : players) {
                        play(player);
                    }
                    subtract(correctX, y, correctZ);
                }
            } case YZ -> {
                for (double angle = startAngle; angle < endAngle; angle = angle + stepAngle) {
                    double radians = Math.toRadians(angle), z = radius * Math.cos(radians), y = radius * Math.sin(radians), correctX = correctX(0, z), correctZ = correctZ(0, z);
                    add(correctX, y, correctZ);
                    for (Player player : players) {
                        play(player);
                    }
                    subtract(correctX, y, correctZ);
                }
            } case XZ -> {
                for (double angle = startAngle; angle < endAngle; angle = angle + stepAngle) {
                    double radians = Math.toRadians(angle), x = radius * Math.sin(radians), z = radius * Math.cos(radians), correctX = correctX(x, z), correctZ = correctZ(x, z);
                    add(correctX, 0, correctZ);
                    for (Player player : players) {
                        play(player);
                    }
                    subtract(correctX, 0, correctZ);
                }
            }
        }
    }

    public void play3D(boolean mirror, double startAngle, double endAngle, double radius, int repeat, double x1, double y1, double z1, double x2, double y2, double z2) {
        List<Player> players = getNearbyEntity(Player.class, x1, y1, z1, x2, y2, z2, null, null);
        double stepAngle = (endAngle - startAngle) / (double) repeat;
        for (double angle = startAngle; angle < endAngle; angle = angle + stepAngle) {
            double radians = Math.toRadians(angle), x = radius * Math.sin(radians), z = radius * Math.cos(radians), y = Math.sin(radians), correctX = mirror ? - correctX(x, z) : correctX(x, z), correctZ = mirror ? - correctZ(x, z) : correctZ(x, z);
            add(correctX, y, correctZ);
            for (Player player : players) {
                play(player);
            }
            subtract(correctX, 0, correctZ);
        }
    }

    public void play2DAnimation(@NotNull AbstractHuanshiPlugin plugin, @NotNull CoordinateType coordinateType, double startAngle, double endAngle, double radius, int repeat, long period, double x1, double y1, double z1, double x2, double y2, double z2) {
        List<Player> players = getNearbyEntity(Player.class, x1, y1, z1, x2, y2, z2, null, null);
        double stepAngle = (endAngle - startAngle) / (double) repeat;
        AtomicDouble atomicDouble = new AtomicDouble(startAngle);
        switch (coordinateType) {
            case XY -> CountdownHelper.start(plugin, false, repeat + 1, 0L, period, null, repeatLeft -> {
                double radians = Math.toRadians(atomicDouble.getAndAdd(stepAngle)), x = radius * Math.sin(radians), y = radius * Math.sin(radians), correctX = correctX(x, 0), correctZ = correctZ(x, 0);
                add(correctX, y, correctZ);
                for (Player player : players) {
                    play(player);
                }
                subtract(correctX, y, correctZ);
                return true;
            }, null);
            case YZ -> CountdownHelper.start(plugin, false, repeat + 1, 0L, period, null, repeatLeft -> {
                double radians = Math.toRadians(atomicDouble.getAndAdd(stepAngle)), z = radius * Math.cos(radians), y = radius * Math.sin(radians), correctX = correctX(0, z), correctZ = correctZ(0, z);
                add(correctX, y, correctZ);
                for (Player player : players) {
                    play(player);
                }
                subtract(correctX, y, correctZ);
                return true;
            }, null);
            case XZ -> CountdownHelper.start(plugin, false, repeat + 1, 0L, period, null, repeatLeft -> {
                double radians = Math.toRadians(atomicDouble.getAndAdd(stepAngle)), x = radius * Math.sin(radians), z = radius * Math.cos(radians), correctX = correctX(x, z), correctZ = correctZ(x, z);
                add(correctX, 0, correctZ);
                for (Player player : players) {
                    play(player);
                }
                subtract(correctX, 0, correctZ);
                return true;
            }, null);
        }
    }

    public void play3DAnimation(@NotNull AbstractHuanshiPlugin plugin, boolean mirror, double startAngle, double endAngle, double radius, int repeat, long period, double x1, double y1, double z1, double x2, double y2, double z2) {
        List<Player> players = getNearbyEntity(Player.class, x1, y1, z1, x2, y2, z2, null, null);
        double stepAngle = (endAngle - startAngle) / (double) repeat;
        AtomicDouble atomicDouble = new AtomicDouble(startAngle);
        CountdownHelper.start(plugin, false, repeat + 1, 0L, period, null, repeatLeft -> {
            double radians = Math.toRadians(atomicDouble.getAndAdd(stepAngle)), x = radius * Math.sin(radians), z = radius * Math.cos(radians), y = Math.sin(radians), correctX = mirror ? - correctX(x, z) : correctX(x, z), correctZ = mirror ? - correctZ(x, z) : correctZ(x, z);
            add(correctX, y, correctZ);
            for (Player player : players) {
                play(player);
            }
            subtract(correctX, y, correctZ);
            return true;
        }, null);
    }
}
