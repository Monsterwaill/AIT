package io.mdt.ait.tardis;

import com.mdt.ait.core.init.AITBlocks;
import io.mdt.ait.nbt.wrapped.AbsoluteBlockPos;
import io.mdt.ait.tardis.link.impl.TARDISLinkableBasic;
import io.mdt.ait.util.TARDISUtil;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TARDISTravel extends TARDISLinkableBasic {

    private State state;
    private CompoundNBT nbtBuffer;

    public TARDISTravel() {
        this(State.IDLE);
    }

    public TARDISTravel(State state) {
        this.state = state;
    }

    public void to(AbsoluteBlockPos pos) {
        if (this.state == State.DEMAT || this.state == State.REMAT) return;

        TileEntity tile = this.getExterior().getTile();
        this.nbtBuffer = tile.getTileData();

        tile.getLevel().setBlock(tile.getBlockPos(), Blocks.AIR.defaultBlockState(), 1);

        World world = TARDISUtil.getWorld(pos.getDimension());
        world.setBlockAndUpdate(pos, AITBlocks.TARDIS_BLOCK.get().defaultBlockState());
        world.getBlockEntity(pos).load(world.getBlockState(pos), this.nbtBuffer);

        this.getTARDIS().setPosition(pos);
        this.getExterior().link(this.getTARDIS());
    }

    public void setState(State state) {
        this.state = state;
    }

    public enum State {
        IDLE,
        DEMAT,
        VORTEX,
        REMAT,
    }
}
