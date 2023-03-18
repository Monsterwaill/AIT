package io.mdt.ait.tardis.link;

import io.mdt.ait.tardis.TARDIS;

public class TARDISLinkRestricted extends TARDISLink {

    @Override
    public void link(TARDIS tardis) {
        throw new IllegalArgumentException("You can't link this TARDISLink!");
    }
}
