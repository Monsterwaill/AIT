package io.mdt.ait.world;

import com.mdt.ait.AIT;
import io.mdt.ait.tardis.TARDISManager;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.storage.WorldSavedData;

/**
 * Saves TARDIS dimension data
 *
 * @implNote WorldSavedData doesn't exist in later versions, once updated to later versions this
 *     won't work
 */
public class TARDISWorldData extends WorldSavedData {

    private static final TARDISManager.Serializer MANAGER_SERIALIZER = new TARDISManager.Serializer();

    public TARDISWorldData() {
        super(AIT.MOD_ID);
    }

    @Override
    public void load(CompoundNBT nbt) {
        MANAGER_SERIALIZER.unserialize(nbt);
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        MANAGER_SERIALIZER.serialize(nbt, TARDISManager.getInstance());
        return nbt;
    }
}
