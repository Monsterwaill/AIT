package io.mdt.ait.tardis.portal;

import io.mdt.ait.util.math.Vector2d;
import net.minecraft.util.math.vector.Vector3d;

public class Portal3i {

    private final Vector2d size;
    private final Vector3d pos;

    public Portal3i(double width, double height, double x, double y, double z) {
        this(new Vector2d(width, height), new Vector3d(x, y, z));
    }

    public Portal3i(Vector2d size, double x, double y, double z) {
        this(size, new Vector3d(x, y, z));
    }

    public Portal3i(double width, double height, Vector3d pos) {
        this(new Vector2d(width, height), pos);
    }

    public Portal3i(Vector2d size, Vector3d pos) {
        this.size = size;
        this.pos = pos;
    }

    public Vector2d size() {
        return this.size;
    }
    public Vector3d pos() {
        return this.pos;
    }

    public double width() {
        return this.size.x;
    }
    public double height() {
        return this.size.y;
    }

    public double x() {
        return this.pos.x;
    }
    public double y() {
        return this.pos.y;
    }
    public double z() {
        return this.pos.z;
    }
}
