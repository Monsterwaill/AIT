package io.mdt.ait.util.math;

public class Vector2d {
    public static final Vector2d ZERO = new Vector2d(0.0F, 0.0F);
    public static final Vector2d ONE = new Vector2d(1.0F, 1.0F);
    public static final Vector2d UNIT_X = new Vector2d(1.0F, 0.0F);
    public static final Vector2d NEG_UNIT_X = new Vector2d(-1.0F, 0.0F);
    public static final Vector2d UNIT_Y = new Vector2d(0.0F, 1.0F);
    public static final Vector2d NEG_UNIT_Y = new Vector2d(0.0F, -1.0F);
    public static final Vector2d MAX = new Vector2d(Double.MAX_VALUE, Double.MAX_VALUE);
    public static final Vector2d MIN = new Vector2d(Double.MIN_VALUE, Double.MIN_VALUE);
    
    public final double x;
    public final double y;

    public Vector2d(double pX, double pY) {
        this.x = pX;
        this.y = pY;
    }

    public boolean equals(Vector2d pOther) {
        return this.x == pOther.x && this.y == pOther.y;
    }
}
