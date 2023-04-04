package io.mdt.ait.tardis.link;

import io.mdt.ait.tardis.TARDIS;
import java.util.Optional;

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
