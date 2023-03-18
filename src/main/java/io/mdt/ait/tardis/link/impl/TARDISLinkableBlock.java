package io.mdt.ait.tardis.link.impl;

import io.mdt.ait.tardis.link.TARDISLink;
import io.mdt.ait.tardis.link.TARDISLinkable;
import net.minecraft.block.Block;

public abstract class TARDISLinkableBlock extends Block implements TARDISLinkable {

    private final TARDISLink link = new TARDISLink();

    public TARDISLinkableBlock(Properties properties) {
        super(properties);
    }

    @Override
    public TARDISLink getLink() {
        return this.link;
    }
}
