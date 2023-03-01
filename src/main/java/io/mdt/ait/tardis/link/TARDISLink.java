package io.mdt.ait.tardis.link;

import io.mdt.ait.tardis.TARDIS;
import io.mdt.ait.tardis.door.TARDISDoor;
import io.mdt.ait.tardis.exterior.TARDISExteriorSchema;
import io.mdt.ait.tardis.interior.TARDISInteriorSchema;

import java.util.Optional;
import java.util.UUID;

/**
 * Class that contains TARDIS reference and other TARDIS linked data.
 *
 * @implNote Returns {@link Optional} in getters, because linking might get delayed.
 */
public class TARDISLink {

    private TARDIS tardis;
    private boolean linked = false;

    public TARDISLink() {

    }

    public void link(TARDIS tardis) {
        this.tardis = tardis;
        this.linked = true;
    }

    public Optional<TARDIS> getTARDIS() {
        return Optional.ofNullable(this.tardis);
    }

    public Optional<UUID> getUUID() {
        if (this.tardis != null) {
            return Optional.of(this.tardis.getUUID());
        }

        return Optional.empty();
    }

    public Optional<TARDISDoor> getDoor() {
        if (this.tardis != null) {
            return Optional.of(this.tardis.getDoor());
        }

        return Optional.empty();
    }

    public Optional<TARDISExteriorSchema> getExterior() {
        if (this.tardis != null) {
            return Optional.of(this.tardis.getExterior());
        }

        return Optional.empty();
    }

    public Optional<TARDISInteriorSchema> getInterior() {
        if (this.tardis != null) {
            return Optional.of(this.tardis.getInterior());
        }

        return Optional.empty();
    }

    public boolean isLinked() {
        return this.linked;
    }
}
