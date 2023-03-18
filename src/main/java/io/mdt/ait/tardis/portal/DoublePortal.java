package io.mdt.ait.tardis.portal;

import com.qouteall.immersive_portals.portal.Portal;
import com.qouteall.immersive_portals.portal.PortalManipulation;
import java.util.function.Function;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class DoublePortal {

    private Portal portal;

    public DoublePortal() {}

    public DoublePortal from(World world, double x, double y, double z, double w, double h) {
        return this.from(world, new Vector3d(x, y, z), w, h);
    }

    public DoublePortal from(World world, Vector3d vector, double w, double h) {
        this.portal = Portal.entityType.create(world);
        this.portal.setOriginPos(vector);
        this.portal.setOrientationAndSize(new Vector3d(1.0D, 0.0D, 0.0D), new Vector3d(0.0D, 1.0D, 0.0D), w, h);

        return this;
    }

    public DoublePortal to(RegistryKey<World> dimension, double x, double y, double z, Quaternion rotation) {
        return this.to(dimension, x, y, z, rotation, 1.0D);
    }

    public DoublePortal to(RegistryKey<World> dimension, Vector3d vector, Quaternion rotation) {
        return this.to(dimension, vector, rotation, 1.0D);
    }

    public DoublePortal to(
            RegistryKey<World> dimension, double x, double y, double z, Quaternion rotation, double scale) {
        return this.to(dimension, new Vector3d(x, y, z), rotation, scale);
    }

    public DoublePortal to(RegistryKey<World> dimension, Vector3d vector, Quaternion rotation, double scale) {
        portal.setDestinationDimension(dimension);
        PortalManipulation.setPortalTransformation(portal, dimension, vector, rotation, scale);

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

    public boolean isBuilt() {
        return this.portal != null && this.portal.isAlive();
    }

    public void setPortal(Portal portal) {
        this.portal = portal;
    }
}
