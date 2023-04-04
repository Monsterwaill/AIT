package io.mdt.ait.tardis.link.impl;

import io.mdt.ait.tardis.link.TARDISLink;
import io.mdt.ait.tardis.link.TARDISLinkable;
import net.minecraft.block.FallingBlock;

public abstract class TARDISLinkableFallingBlock extends FallingBlock implements TARDISLinkable {

    private final TARDISLink link = new TARDISLink();

    public TARDISLinkableFallingBlock(Properties properties) {
        super(properties);
    }

    @Override
    public TARDISLink getLink() {
        return this.link;
    }
}
