package io.mdt.ait.tardis.link.impl;

import io.mdt.ait.tardis.link.ITARDISLinkable;
import io.mdt.ait.tardis.link.TARDISLink;
import net.minecraft.item.Item;

public abstract class TARDISLinkableItem extends Item implements ITARDISLinkable {

    private final TARDISLink link = new TARDISLink();

    public TARDISLinkableItem(Properties properties) {
        super(properties);
    }

    @Override
    public TARDISLink getLink() {
        return this.link;
    }
}
