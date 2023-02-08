package io.mdt.ait.tardis;

import com.qouteall.immersive_portals.portal.Portal;
import com.qouteall.immersive_portals.portal.PortalManipulation;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.function.Function;

public class DoublePortal {

    private Portal portal;

    public DoublePortal() { }

    public DoublePortal from(World world, double x, double y, double z, double w, double h) {
        return this.from(world, new Vector3d(x, y, z), w, h);
    }

    public DoublePortal from(World world, Vector3d vector, double w, double h) {
        this.portal = Portal.entityType.create(world);
        this.portal.setOriginPos(vector);
        this.portal.setOrientationAndSize(
                new Vector3d(1.0D, 0.0D, 0.0D),
                new Vector3d(0.0D, 1.0D, 0.0D),
                w, h
        );

        return this;
    }

    public DoublePortal to(World world, Vector3d vector, Quaternion rotation) {
        return this.to(world, vector, rotation, 1.0D);
    }

    public DoublePortal to(World world, Vector3d vector, Quaternion rotation, double scale) {
        portal.setDestinationDimension(world.dimension());
        PortalManipulation.setPortalTransformation(portal, world.dimension(), vector, rotation, scale);

        return this;
    }

    public DoublePortal apply(Function<Portal, Portal> function) {
        function.apply(this.portal);
        return this;
    }

    public void build() {
        this.portal = PortalManipulation.completeBiWayPortal(this.portal, Portal.entityType);
        this.portal.reloadAndSyncToClient();
    }

    public Portal get() {
        return this.portal;
    }
}
