package io.mdt.ait.tardis;

import io.mdt.ait.nbt.NBTDeserializer;
import io.mdt.ait.nbt.NBTSerializer;
import io.mdt.ait.tardis.builder.TARDISBuilder;
import io.mdt.ait.tardis.exterior.TARDISExteriors;
import io.mdt.ait.tardis.interior.TARDISInteriors;
import io.mdt.ait.util.TARDISUtil;
import io.mdt.ait.world.TARDISWorldData;
import java.util.UUID;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TARDISManager {

    private static final TARDISManager instance = new TARDISManager();

    private final TARDISMap tardises = new TARDISMap();
    private final TARDISWorldData savedData = new TARDISWorldData();

    /**
     * Creates a TARDIS instance.
     *
     * @param position position where the TARDIS should be created
     * @param dimension
     * @return
     */
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
     * @return int
     * @implNote DO NOT USE THIS METHOD UNDER ANY CIRCUMSTANCES. IT IS MADE FOR {@link TARDIS} CLASS.
     */
    public int getLastIndex() {
        return tardises.size()
                + 1; // Need to add 1, because when the TARDIS is created, it is not being put into the map
        // right away.
    }

    public static class Serializer implements NBTSerializer<TARDISManager>, NBTDeserializer<TARDISManager> {

        private static final TARDISMap.Serializer MAP_SERIALIZER = new TARDISMap.Serializer();

        @Override
        public void serialize(CompoundNBT nbt, TARDISManager manager) {
            MAP_SERIALIZER.serialize(nbt, manager.getTARDISMap());
            manager.getSavedData().setDirty(false);
        }

        @Override
        public TARDISManager deserialize(CompoundNBT nbt) {
            TARDISManager manager = TARDISManager.getInstance();

            manager.getTARDISMap().set(MAP_SERIALIZER.deserialize(nbt));
            manager.getSavedData().setDirty(false);
            return manager;
        }
    }
}
