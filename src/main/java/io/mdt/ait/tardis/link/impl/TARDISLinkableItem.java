package io.mdt.ait.tardis.link.impl;

import io.mdt.ait.tardis.link.TARDISLink;
import io.mdt.ait.tardis.link.TARDISLinkable;
import net.minecraft.item.Item;

public abstract class TARDISLinkableItem extends Item implements TARDISLinkable {

    private final TARDISLink link = new TARDISLink();

    public TARDISLinkableItem(Properties properties) {
        super(properties);
    }

    @Override
    public TARDISLink getLink() {
        return this.link;
    }
}
