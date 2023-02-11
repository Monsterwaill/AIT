package io.mdt.ait.tardis;

import io.mdt.ait.tardis.exterior.TARDISExterior;
import io.mdt.ait.tardis.interior.TARDISInterior;

import java.util.Optional;
import java.util.UUID;

/**
 * Used for identifying objects that need to be linked to tardis
 *
 * @implNote delegates methods to {@link TARDISLink} and unwraps {@link Optional}s.
 */
public interface ITARDISLinked {
    TARDISLink getLink();

    default boolean isLinked() {
        return this.getLink().isLinked();
    }

    default TARDIS getTARDIS() {
        return this.getLink().getTARDIS().get();
    }

    default UUID getUUID() {
        return this.getLink().getUUID().get();
    }

    default TARDISDoor getDoor() {
        return this.getLink().getDoor().get();
    }

    default TARDISExterior getExterior() {
        return this.getLink().getExterior().get();
    }

    default TARDISInterior getInterior() {
        return this.getLink().getInterior().get();
    }
}
