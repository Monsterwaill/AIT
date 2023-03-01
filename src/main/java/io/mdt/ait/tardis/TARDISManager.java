package io.mdt.ait.tardis;

import io.mdt.ait.nbt.NBTSerializeable;
import io.mdt.ait.nbt.NBTUnserializeable;
import io.mdt.ait.tardis.builder.TARDISBuilder;
import io.mdt.ait.tardis.exterior.TARDISExteriors;
import io.mdt.ait.tardis.interior.TARDISInteriors;
import io.mdt.ait.util.TARDISUtil;
import io.mdt.ait.world.TARDISWorldData;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.UUID;

public class TARDISManager {

    private static final TARDISManager instance = new TARDISManager();

    private final TARDISMap tardises = new TARDISMap();
    private final TARDISWorldData savedData = new TARDISWorldData();


    public TARDIS create(BlockPos position, RegistryKey<World> dimension) {
        savedData.setDirty(true);

        TARDIS tardis = new TARDISBuilder(UUID.randomUUID())
                .at(position, dimension)
                .exterior(TARDISExteriors.get("basic"))
                .interior(TARDISInteriors.get("dev"))
                .build();

        this.tardises.put(tardis);
        return tardis;
    }

    public TARDIS findTARDIS(UUID uuid) {
        return this.tardises.get(uuid);
    }

    public UUID findUUID(int index) {
        return this.tardises.get(index);
    }

    public TARDIS findTARDIS(BlockPos pos) {
        return this.findTARDIS(this.findUUID(pos));
    }

    public UUID findUUID(BlockPos pos) {
        return this.findUUID(TARDISUtil.getIndexByPos(pos));
    }

    public TARDISMap getTARDISMap() {
        return this.tardises;
    }

    public TARDISWorldData getSavedData() {
        return this.savedData;
    }

    public static TARDISManager getInstance() {
        return instance;
    }

    /**
     *
     * @return int
     * @implNote DO NOT USE THIS METHOD UNDER ANY CIRCUMSTANCES. IT IS MADE FOR {@link TARDIS} CLASS.
     */
    public int getLastIndex() {
        return tardises.size() + 1; // Need to add 1, because when the TARDIS is created, it is not being put into the map right away.
    }

    public static class Serializer implements NBTSerializeable<TARDISManager>, NBTUnserializeable<TARDISManager> {

        private static final TARDISMap.Serializer MAP_SERIALIZER = new TARDISMap.Serializer();
        private static final TARDISManager MANAGER = TARDISManager.getInstance();

        @Override
        public void serialize(TARDISManager manager, CompoundNBT nbt) {
            MAP_SERIALIZER.serialize(MANAGER.getTARDISMap(), nbt);
            MANAGER.getSavedData().setDirty(false);
        }

        /**
         *
         * @param nbt NBT that needs to be unserialized
         * @return Singleton instance of {@link TARDISManager}
         */
        @Override
        public TARDISManager unserialize(CompoundNBT nbt) {
            MANAGER.getTARDISMap().set(MAP_SERIALIZER.unserialize(nbt));
            MANAGER.getSavedData().setDirty(false);
            return MANAGER;
        }
    }
}
