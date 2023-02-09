package io.mdt.ait.tardis;

import io.mdt.ait.tardis.exterior.TARDISExterior;
import io.mdt.ait.tardis.interior.TARDISInterior;

import java.util.Optional;
import java.util.UUID;

/**
 * Used for identifying objects that need to be linked to tardis
 *
 * @implNote delegates some methods to {@link TARDISLink}.
 */
public interface ITARDISLinked {
    TARDISLink getLink();

    default boolean isLinked() {
        return this.getLink().isLinked();
    }

    default Optional<TARDIS> getTARDIS() {
        return this.getLink().getTARDIS();
    }

    default Optional<UUID> getUUID() {
        return this.getLink().getUUID();
    }

    default Optional<TARDISDoor> getDoor() {
        return this.getLink().getDoor();
    }

    default Optional<TARDISExterior> getExterior() {
        return this.getLink().getExterior();
    }

    default Optional<TARDISInterior> getInterior() {
        return this.getLink().getInterior();
    }
}
