package io.mdt.ait.tardis.door;

import com.mdt.ait.core.init.AITSounds;
import io.mdt.ait.nbt.NBTDeserializer;
import io.mdt.ait.nbt.NBTSerializerStatic;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.SoundEvent;

/*public class TARDISDoorState {
    private final TARDISDoorStates[] states =
            new TARDISDoorStates[] {TARDISDoorStates.CLOSED, TARDISDoorStates.FIRST, TARDISDoorStates.BOTH};

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

    public static class Serializer implements NBTSerializer<TARDISDoorState>, NBTDeserializer<TARDISDoorState> {

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
}*/

public enum TARDISDoorState {
    CLOSED() {
        @Override
        public TARDISDoorState next() {
            return FIRST;
        }

        @Override
        public SoundEvent sound() {
            return AITSounds.POLICE_BOX_CLOSE.get();
        }
    },
    FIRST() {
        @Override
        public TARDISDoorState next() {
            return BOTH;
        }

        @Override
        public SoundEvent sound() {
            return AITSounds.POLICE_BOX_OPEN.get();
        }
    },
    BOTH() {
        @Override
        public TARDISDoorState next() {
            return CLOSED;
        }

        @Override
        public SoundEvent sound() {
            return AITSounds.POLICE_BOX_OPEN.get();
        }
    },
    LOCKED() {
        @Override
        public TARDISDoorState next() {
            return LOCKED;
        }

        @Override
        public SoundEvent sound() {
            return AITSounds.TARDIS_LOCK.get();
        }
    };

    public abstract TARDISDoorState next();

    public abstract SoundEvent sound(); // sound that will play when state A will change to state B -> B.sound();

    public static TARDISDoorState defaultValue() {
        return CLOSED;
    }

    public static class Serializer implements NBTSerializerStatic<TARDISDoorState>, NBTDeserializer<TARDISDoorState> {

        @Override
        public void serialize(CompoundNBT nbt, TARDISDoorState state) {
            nbt.putInt("door", state.ordinal());
        }

        @Override
        public TARDISDoorState unserialize(CompoundNBT nbt) {
            return TARDISDoorState.values()[nbt.getInt("door")];
        }
    }
}
