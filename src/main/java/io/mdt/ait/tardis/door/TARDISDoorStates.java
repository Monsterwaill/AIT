package io.mdt.ait.tardis.door;

import com.mdt.ait.core.init.AITSounds;
import net.minecraft.util.SoundEvent;

public enum TARDISDoorStates {
    FIRST(AITSounds.POLICE_BOX_OPEN.get()),
    BOTH(AITSounds.POLICE_BOX_OPEN.get()),
    CLOSED(AITSounds.POLICE_BOX_CLOSE.get()),
    LOCKED(AITSounds.TARDIS_LOCK.get());

    private final SoundEvent sound;

    TARDISDoorStates(SoundEvent sound) {
        this.sound = sound;
    }

    public SoundEvent getSound() {
        return this.sound;
    }
}
