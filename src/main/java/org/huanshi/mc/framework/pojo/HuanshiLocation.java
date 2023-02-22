package org.huanshi.mc.framework.pojo;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R2.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

public class HuanshiLocation extends Location {
    private final double sin, cos;

    public HuanshiLocation(@NotNull Location location) {
        super(location.getWorld(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        double radians = Math.toRadians(location.getYaw());
        sin = Math.sin(radians);
        cos = Math.cos(radians);
    }

    public @NotNull Location getLocation(double x, double y, double z) {
        return clone().set(getX(x, z), getY(y), getZ(x, z));
    }

    public @NotNull Vector getDirection(int distance) {
        float pitch = getPitch();
        setPitch(0.0F);
        Vector direction = getDirection().multiply(distance);
        setPitch(pitch);
        return direction;
    }

    public @NotNull AABB getAABB(double x1, double y1, double z1, double x2, double y2, double z2) {
        return new AABB(new Vector(getX(x1, z1), getY(y1), getZ(x1, z1)), new Vector(getX(-x2, z2), getY(y1), getZ(-x2, z2)), new Vector(getX(x2, z2), getY(y2), getZ(x2, z2)), new Vector(getX(-x1, z1), getY(y2), getZ(-x1, z1)));
    }

    public @NotNull <T extends Entity> List<T> getNearbyEntity(@NotNull Class<T> clazz, double x1, double y1, double z1, double x2, double y2, double z2, @Nullable Predicate<T> predicate, @Nullable EntityHandler<T> entityHandler) {
        return getNearbyEntityExceptPlayer(null, clazz, x1, y1, z1, x2, y2, z2, predicate, entityHandler);
    }

    @SuppressWarnings("unchecked")
    public @NotNull <T extends Entity> List<T> getNearbyEntityExceptPlayer(@Nullable Player player, @NotNull Class<T> clazz, double x1, double y1, double z1, double x2, double y2, double z2, @Nullable Predicate<T> predicate, @Nullable EntityHandler<T> entityHandler) {
        List<T> entityList = new LinkedList<>();
        AABB aabb = getAABB(x1, y1, z1, x2, y2, z2);
        ((CraftWorld) getWorld()).getHandle().getEntityLookup().getEntities(player == null ? null : ((CraftEntity) player).getHandle(), aabb.getAxisAlignedBB(), null, entity -> {
            CraftEntity craftEntity = entity.getBukkitEntity();
            if (clazz.isInstance(craftEntity)) {
                T t = (T) craftEntity;
                if ((predicate == null || predicate.test(t)) && aabb.isInAABB(t)) {
                    if (entityHandler != null) {
                        entityHandler.handle(t);
                    }
                    entityList.add(t);
                }
            }
            return false;
        });
        return entityList;
    }

    private double getX(double x, double z) {
        return getX() - x * cos - z * sin;
    }

    private double getY(double y) {
        return getY() + y;
    }

    private double getZ(double x, double z) {
        return getZ() + z * cos - x * sin;
    }
}
