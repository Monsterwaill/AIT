package io.mdt.ait.tardis.link;

import io.mdt.ait.tardis.TARDIS;
import io.mdt.ait.tardis.door.TARDISDoor;
import io.mdt.ait.tardis.exterior.TARDISExteriorSchema;
import io.mdt.ait.tardis.interior.TARDISInteriorSchema;

import java.util.Optional;
import java.util.UUID;

/**
 * Used for identifying objects that need to be linked to a TARDIS
 *
 * @implNote delegates methods to {@link TARDISLink} and unwraps {@link Optional}s.
 */
public interface ITARDISLinkable {
    TARDISLink getLink();

    default boolean isLinked() {
        return this.getLink().isLinked();
    }

    default void link(TARDIS tardis) {
        this.getLink().link(tardis);
    }

    default TARDIS getTARDIS() {
        return this.getLink().getTARDIS().orElse(null);
    }

    default UUID getUUID() {
        return this.getLink().getUUID().orElse(null);
    }

    default TARDISDoor getDoor() {
        return this.getLink().getDoor().orElse(null);
    }

    default TARDISExteriorSchema getExterior() {
        return this.getLink().getExterior().orElse(null);
    }

    default TARDISInteriorSchema getInterior() {
        return this.getLink().getInterior().orElse(null);
    }
}
