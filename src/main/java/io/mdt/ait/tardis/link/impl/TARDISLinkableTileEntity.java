package io.mdt.ait.tardis.link.impl;

import io.mdt.ait.tardis.TARDISManager;
import io.mdt.ait.tardis.link.TARDISLink;
import io.mdt.ait.tardis.link.TARDISLinkable;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

public abstract class TARDISLinkableTileEntity extends TileEntity implements TARDISLinkable {

    private final TARDISLink link = new TARDISLink();

    public TARDISLinkableTileEntity(TileEntityType<?> type) {
        super(type);
    }

    @Override
    public TARDISLink getLink() {
        return this.link;
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        if (nbt.contains("tardis")) {
            // Using #getLink() here to make sure not to trigger overriden link method!
            this.getLink().link(TARDISManager.getInstance().findTARDIS(nbt.getUUID("tardis")));
        }

        super.load(state, nbt);
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        if (this.isLinked()) {
            nbt.putUUID("tardis", this.getUUID());
        }

        return super.save(nbt);
    }

    public void sync() {
        if (this.level != null) {
            this.level.setBlocksDirty(
                    this.worldPosition,
                    this.level.getBlockState(this.worldPosition),
                    this.level.getBlockState(this.worldPosition));

            this.level.sendBlockUpdated(
                    this.worldPosition,
                    this.level.getBlockState(this.worldPosition),
                    this.level.getBlockState(this.worldPosition),
                    3);

            this.setChanged();
        }
    }
}
