package io.mdt.ait.tardis.link.impl;

import io.mdt.ait.tardis.link.ITARDISLinkable;
import io.mdt.ait.tardis.link.TARDISLink;

public abstract class TARDISLinkableBasic implements ITARDISLinkable {

    private final TARDISLink link = new TARDISLink();

    @Override
    public TARDISLink getLink() {
        return this.link;
    }
}
