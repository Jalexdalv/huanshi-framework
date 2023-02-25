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

public class LocationHelper extends Location {
    private final double sin, cos;

    public LocationHelper(@NotNull Location location) {
        super(location.getWorld(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        double radians = Math.toRadians(location.getYaw());
        sin = Math.sin(radians);
        cos = Math.cos(radians);
    }

    public double correctX(double x, double z) {
        return - x * cos - z * sin;
    }

    public double correctZ(double x, double z) {
        return z * cos - x * sin;
    }

    public @NotNull Location correctLocation(double x, double y, double z) {
        return clone().add(correctX(x, z), y, correctZ(x, z));
    }

    public @NotNull Vector correctDirection(int distance) {
        float pitch = getPitch();
        setPitch(0.0F);
        Vector direction = getDirection().multiply(distance);
        setPitch(pitch);
        return direction;
    }

    public @NotNull AABB getAABB(double x1, double y1, double z1, double x2, double y2, double z2) {
        return new AABB(new Vector(getX() + correctX(x1, z1), getY() + y1, getZ() + correctZ(x1, z1)), new Vector(getX() + correctX(-x2, z2), getY() + y1, getZ() + correctZ(-x2, z2)), new Vector(getX() + correctX(x2, z2), getY() + y2, getZ() + correctZ(x2, z2)), new Vector(getX() + correctX(-x1, z1), getY() + y2, getZ() + correctZ(-x1, z1)));
    }

    public @NotNull <T extends Entity> List<T> getNearbyEntity(@NotNull Class<T> clazz, double x1, double y1, double z1, double x2, double y2, double z2, @Nullable Predicate<T> predicate, @Nullable IEntityHandler<T> entityHandler) {
        return getNearbyEntityExceptPlayer(null, clazz, x1, y1, z1, x2, y2, z2, predicate, entityHandler);
    }

    @SuppressWarnings("unchecked")
    public @NotNull <T extends Entity> List<T> getNearbyEntityExceptPlayer(@Nullable Player player, @NotNull Class<T> clazz, double x1, double y1, double z1, double x2, double y2, double z2, @Nullable Predicate<T> predicate, @Nullable IEntityHandler<T> entityHandler) {
        List<T> entities = new LinkedList<>();
        AABB aabb = getAABB(x1, y1, z1, x2, y2, z2);
        ((CraftWorld) getWorld()).getHandle().getEntityLookup().getEntities(player == null ? null : ((CraftEntity) player).getHandle(), aabb.getAxisAlignedBB(), null, entity -> {
            CraftEntity craftEntity = entity.getBukkitEntity();
            if (clazz.isInstance(craftEntity)) {
                T t = (T) craftEntity;
                if ((predicate == null || predicate.test(t)) && aabb.isInAABB(t)) {
                    if (entityHandler != null) {
                        entityHandler.handle(t);
                    }
                    entities.add(t);
                }
            }
            return false;
        });
        return entities;
    }
}
