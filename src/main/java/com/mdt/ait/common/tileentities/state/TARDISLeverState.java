package com.mdt.ait.common.tileentities.state;

import io.mdt.ait.tardis.state.TARDISComponentState;
import net.minecraft.nbt.CompoundNBT;

public class TARDISLeverState extends TARDISComponentState<TARDISLeverState> {

    private State state;

    public TARDISLeverState() {
        this(State.INACTIVE);
    }

    public TARDISLeverState(State state) {
        super("lever", new Serializer());

        this.state = state;
    }

    public State getLeverState() {
        return this.state;
    }

    public void setLeverState(State state) {
        this.state = state;
    }

    public static class Serializer extends TARDISComponentState.Serializer<TARDISLeverState> {

        public Serializer() {
            super(TARDISLeverState.class);
        }

        @Override
        public void serializeState(CompoundNBT nbt, TARDISLeverState state) {
            nbt.putInt("lever_state", state.getLeverState().ordinal());
        }

        @Override
        public TARDISLeverState deserializeState(CompoundNBT nbt) {
            return new TARDISLeverState(State.values()[nbt.getInt("lever_state")]);
        }
    }

    public enum State {
        INACTIVE() {
            @Override
            public State next() {
                return ACTIVE;
            }
        },
        ACTIVE() {
            @Override
            public State next() {
                return INACTIVE;
            }
        };

        public abstract State next();
    }
}
