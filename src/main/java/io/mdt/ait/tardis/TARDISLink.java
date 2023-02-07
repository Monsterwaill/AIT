package io.mdt.ait.tardis;

import java.util.Optional;
import java.util.UUID;

/**
 * Class that contains TARDIS reference and other TARDIS linked data.
 *
 * @implNote Returns {@link Optional} in getters, because linking might get delayed.
 */
public class TARDISLink {

    private TARDIS tardis;

    public TARDISLink() {

    }

    public void link(TARDIS tardis) {
        this.tardis = tardis;
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
}
