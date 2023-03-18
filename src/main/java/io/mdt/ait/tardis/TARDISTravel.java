package io.mdt.ait.tardis;

import io.mdt.ait.tardis.link.impl.TARDISLinkableBasic;
import net.minecraft.util.math.BlockPos;

public class TARDISTravel extends TARDISLinkableBasic {

    private State state;

    public TARDISTravel() {
        this(State.IDLE);
    }

    public TARDISTravel(State state) {
        this.state = state;
    }

    public void to(BlockPos pos) {}

    public enum State {
        IDLE,
        DEMAT,
        VORTEX,
        LAND,
    }
}
