package com.mdt.ait.common.tileentities.state;

import io.mdt.ait.tardis.state.TARDISComponentState;
import net.minecraft.nbt.CompoundNBT;

public class ExteriorFacingControlState extends TARDISComponentState<ExteriorFacingControlState> {

    public ExteriorFacingControlState() {
        super("facing", new Serializer());
    }

    public static class Serializer extends TARDISComponentState.Serializer<ExteriorFacingControlState> {

        public Serializer() {
            super(ExteriorFacingControlState.class);
        }

        @Override
        public void serializeState(CompoundNBT nbt, ExteriorFacingControlState state) {}

        @Override
        public ExteriorFacingControlState deserializeState(CompoundNBT nbt) {
            return new ExteriorFacingControlState();
        }
    }
}
