package io.mdt.ait.tardis.link.impl.stateful;

import io.mdt.ait.tardis.TARDIS;
import io.mdt.ait.tardis.link.impl.TARDISLinkableTileEntity;
import io.mdt.ait.tardis.state.TARDISComponentState;
import net.minecraft.tileentity.TileEntityType;

public abstract class TARDISComponent<T extends TARDISComponentState<?>> extends TARDISLinkableTileEntity {

    public TARDISComponent(TileEntityType<?> type) {
        super(type);
    }

    @Override
    public void link(TARDIS tardis) {
        super.link(tardis);
        this.getStateManager().add(this.createState());
    }

    public abstract TARDISComponentState<T> createState();
}
