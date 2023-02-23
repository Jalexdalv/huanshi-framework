package org.huanshi.mc.framework.pojo;

import net.minecraft.world.phys.AxisAlignedBB;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class AABB {
    protected AxisAlignedBB axisAlignedBB;
    protected Vector vector1, vector2, vector3, vector4, vector12, vector34, vector23, vector41;

    public AABB(@NotNull Vector vector1, @NotNull Vector vector2, @NotNull Vector vector3, @NotNull Vector vector4) {
        this.vector1 = vector1;
        this.vector2 = vector2;
        this.vector3 = vector3;
        this.vector4 = vector4;
        vector12 = vector2.clone().subtract(vector1);
        vector34 = vector4.clone().subtract(vector3);
        vector23 = vector3.clone().subtract(vector2);
        vector41 = vector1.clone().subtract(vector4);
        double[] xs = new double[] {vector1.getX(), vector2.getX(), vector3.getX(), vector4.getX()};
        Arrays.sort(xs);
        double[] ys = new double[] {vector1.getY(), vector2.getY(), vector3.getY(), vector4.getY()};
        Arrays.sort(ys);
        double[] zs = new double[] {vector1.getZ(), vector2.getZ(), vector3.getZ(), vector4.getZ()};
        Arrays.sort(zs);
        axisAlignedBB = new AxisAlignedBB(xs[0], ys[0], zs[0], xs[3], ys[3], zs[3]);
    }

    public @NotNull AxisAlignedBB getAxisAlignedBB() {
        return axisAlignedBB;
    }

    public boolean isInAABB(@NotNull Entity entity) {
        Location location = entity.getLocation();
        double x = location.getX(), z = location.getZ();
        return (vector12.getX() * (z - vector1.getZ()) - (x - vector1.getX()) * vector12.getZ()) * (vector34.getX() * (z - vector3.getZ()) - (x - vector3.getX()) * vector34.getZ()) >= 0 && (vector23.getX() * (z - vector2.getZ()) - (x - vector2.getX()) * vector23.getZ()) * (vector41.getX() * (z - vector4.getZ()) - (x - vector4.getX()) * vector41.getZ()) >= 0;
    }
}
