package io.mdt.ait.tardis.link.impl;

import io.mdt.ait.tardis.link.TARDISLink;
import io.mdt.ait.tardis.link.TARDISLinkable;

public abstract class TARDISLinkableBasic implements TARDISLinkable {

    private final TARDISLink link = new TARDISLink();

    @Override
    public TARDISLink getLink() {
        return this.link;
    }
}
