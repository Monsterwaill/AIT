package io.mdt.ait.tardis.link.impl;

import io.mdt.ait.tardis.link.ITARDISLinkable;
import io.mdt.ait.tardis.link.TARDISLink;
import net.minecraft.block.Block;

public class TARDISLinkableBlock extends Block implements ITARDISLinkable {

    protected final TARDISLink link = new TARDISLink();

    public TARDISLinkableBlock(Properties properties) {
        super(properties);
    }

    @Override
    public TARDISLink getLink() {
        return this.link;
    }
}
