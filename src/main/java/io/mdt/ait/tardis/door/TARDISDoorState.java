package io.mdt.ait.tardis.door;

import io.mdt.ait.nbt.NBTSerializeable;
import io.mdt.ait.nbt.NBTUnserializeable;
import net.minecraft.nbt.CompoundNBT;

public class TARDISDoorState {
    private final TARDISDoorStates[] states = new TARDISDoorStates[] {
            TARDISDoorStates.CLOSED, TARDISDoorStates.FIRST, TARDISDoorStates.BOTH
    };

    private int index = 0;
    private boolean isLocked;

    public TARDISDoorStates get() {
        return this.isLocked ? TARDISDoorStates.LOCKED : this.states[index];
    }

    public TARDISDoorStates next() {
        this.index = this.index + 1 == this.states.length ? 0 : this.index + 1;
        return this.get();
    }

    public TARDISDoorStates previous() {
        this.index = this.index == 0 ? this.states.length - 1 : this.index - 1;
        return this.get();
    }

    public boolean isClosed() {
        return this.isLocked || this.get() == TARDISDoorStates.CLOSED;
    }

    public boolean isLocked() {
        return this.isLocked;
    }

    public void lock() {
        this.isLocked = true;
    }

    public void unlock() {
        this.isLocked = false;
    }

    public void setLocked(boolean locked) {
        this.isLocked = locked;
    }


    public static class Serializer implements NBTSerializeable<TARDISDoorState>, NBTUnserializeable<TARDISDoorState> {

        @Override
        public void serialize(CompoundNBT nbt, TARDISDoorState state) {
            nbt.putInt("door", this.getRaw(state));
        }

        @Override
        public TARDISDoorState unserialize(CompoundNBT nbt) {
            return this.fromRaw(new TARDISDoorState(), nbt.getInt("door"));
        }

        private int getRaw(TARDISDoorState state) {
            return !state.isLocked ? state.index : 3;
        }

        private TARDISDoorState fromRaw(TARDISDoorState state, int index) {
            if (index == 3) {
                state.index = 0;
                state.isLocked = true;
            } else {
                state.index = index;
            }

            return state;
        }
    }
}
