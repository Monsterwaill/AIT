package io.mdt.ait.tardis.link;

import io.mdt.ait.tardis.TARDIS;
import io.mdt.ait.tardis.door.TARDISDoor;
import io.mdt.ait.tardis.exterior.TARDISExterior;
import io.mdt.ait.tardis.exterior.TARDISExteriorSchema;
import io.mdt.ait.tardis.interior.TARDISInterior;
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

    public void link(TARDIS tardis) {
        this.tardis = tardis;
        this.linked = true;
    }

    public TARDIS getTARDIS() {
        return this.tardis;
    }

    public boolean isLinked() {
        return this.linked;
    }
}
