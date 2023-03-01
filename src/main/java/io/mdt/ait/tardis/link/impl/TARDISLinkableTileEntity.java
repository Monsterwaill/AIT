package io.mdt.ait.tardis.link.impl;

import io.mdt.ait.tardis.link.ITARDISLinkable;
import io.mdt.ait.tardis.link.TARDISLink;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

public abstract class TARDISLinkableTileEntity extends TileEntity implements ITARDISLinkable {

    protected final TARDISLink link = new TARDISLink();

    public TARDISLinkableTileEntity(TileEntityType<?> type) {
        super(type);
    }

    @Override
    public TARDISLink getLink() {
        return this.link;
    }
}
