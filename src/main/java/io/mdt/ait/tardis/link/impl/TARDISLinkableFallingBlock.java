package io.mdt.ait.tardis.link.impl;

import io.mdt.ait.tardis.link.ITARDISLinkable;
import io.mdt.ait.tardis.link.TARDISLink;
import net.minecraft.block.FallingBlock;

public abstract class TARDISLinkableFallingBlock extends FallingBlock implements ITARDISLinkable {

    private final TARDISLink link = new TARDISLink();

    public TARDISLinkableFallingBlock(Properties properties) {
        super(properties);
    }

    @Override
    public TARDISLink getLink() {
        return this.link;
    }
}
