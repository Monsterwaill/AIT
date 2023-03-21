package io.mdt.ait.tardis.portal;

import com.qouteall.immersive_portals.portal.Portal;
import com.qouteall.immersive_portals.portal.PortalManipulation;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class TARDISPortal {

    private final List<Consumer<Portal>> instructions = new ArrayList<>();
    private final World world;

    private Portal builtPortal;
    private boolean isBuilt = false;

    public TARDISPortal(World world) {
        this.world = world;
    }

    public TARDISPortal from(Vector3d vector) {
        return this.add(portal -> portal.setOriginPos(vector));
    }

    public TARDISPortal to(RegistryKey<World> dimension, Vector3d vector) {
        return this.add(portal -> {
            portal.setDestinationDimension(dimension);
            portal.setDestination(vector);
        });
    }

    public TARDISPortal withOrientationAndSize(Portal3i vector) {
        return this.withOrientationAndSize(vector.width(), vector.height());
    }

    public TARDISPortal withOrientationAndSize(double width, double height) {
        return this.add(portal -> portal.setOrientationAndSize(
                new Vector3d(1.0D, 0.0D, 0.0D), new Vector3d(0.0D, 1.0D, 0.0D), width, height));
    }

    public TARDISPortal withOrientationAndSize(Vector3d axisW, Vector3d axisH, Portal3i vector) {
        return this.withOrientationAndSize(axisW, axisH, vector.width(), vector.height());
    }

    public TARDISPortal withOrientationAndSize(Vector3d axisW, Vector3d axisH, double width, double height) {
        return this.add(portal -> portal.setOrientationAndSize(axisW, axisH, width, height));
    }

    public TARDISPortal rotate(Quaternion rotation) {
        return this.add(portal -> PortalManipulation.rotatePortalBody(portal, rotation));
    }

    public TARDISPortal setRotation(Quaternion rotation) {
        return this.add(portal -> portal.setRotationTransformation(rotation));
    }

    public void build() {
        this.builtPortal = Portal.entityType.create(world);

        for (Consumer<Portal> instruction : this.instructions) {
            instruction.accept(this.builtPortal);
        }

        this.builtPortal = PortalManipulation.completeBiWayPortal(this.builtPortal, Portal.entityType);
        this.builtPortal.reloadAndSyncToClient();

        this.isBuilt = true;
    }

    public TARDISPortal apply(Consumer<Portal> instruction) {
        instruction.accept(this.builtPortal);
        return this;
    }

    public TARDISPortal add(Consumer<Portal> instruction) {
        this.instructions.add(instruction);
        return this;
    }

    public boolean isBuilt() {
        return this.isBuilt;
    }
}
