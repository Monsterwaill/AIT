package io.mdt.ait.tardis.link.impl;

import io.mdt.ait.tardis.link.ITARDISLinkable;
import io.mdt.ait.tardis.link.TARDISLink;

public class TARDISLinkable implements ITARDISLinkable {

    protected final TARDISLink link = new TARDISLink();

    @Override
    public TARDISLink getLink() {
        return this.link;
    }
}
