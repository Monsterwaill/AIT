package io.mdt.ait.tardis.link.impl;

import io.mdt.ait.tardis.TARDISManager;
import io.mdt.ait.tardis.link.TARDISLink;
import io.mdt.ait.tardis.link.TARDISLinkable;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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
        World world = this.getLevel();
        BlockPos pos = this.getBlockPos();

        if (world == null) return;

        world.setBlocksDirty(pos, world.getBlockState(pos), world.getBlockState(pos));

        world.sendBlockUpdated(pos, world.getBlockState(pos), world.getBlockState(pos), 3);

        this.setChanged();
    }
}
