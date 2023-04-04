package io.mdt.ait.tardis;

import io.mdt.ait.nbt.NBTDeserializer;
import io.mdt.ait.nbt.NBTSerializer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.Constants;

public class TARDISMap extends HashMap<UUID, TARDIS> {

    private final List<UUID> references = new ArrayList<>();

    /**
     * Associates the specified value with the specified key in this map. If the map previously
     * contained a mapping for the key, the old value is replaced.
     *
     * @param key key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @return the previous value associated with {@code key}, or {@code null} if there was no mapping
     *     for {@code key}. (A {@code null} return can also indicate that the map previously
     *     associated {@code null} with {@code key})
     */
    @Override
    public TARDIS put(UUID key, TARDIS value) {
        this.references.add(key);
        return super.put(key, value);
    }

    /**
     * Associates the specified TARDIS with its UUID. If the map previously contained a mapping for
     * the key, the old value is replaced.
     *
     * @param value value to be associated with the UUID
     * @return the previous value associated with {@code key}, or {@code null} if there was no mapping
     *     for {@code key}. (A {@code null} return can also indicate that the map previously
     *     associated {@code null} with {@code key})
     */
    @SuppressWarnings("UnusedReturnValue")
    public TARDIS put(TARDIS value) {
        return this.put(value.getUUID(), value);
    }

    /**
     * Returns the TARDIS at the specified position in this map.
     *
     * @param index index of the element to return
     * @return the element at the specified position in this list
     * @throws IndexOutOfBoundsException if the index is out of range ({@code index < 0 || index >=
     *     size()})
     */
    public UUID get(int index) {
        return this.references.get(index);
    }

    // TODO: DOCS
    public List<UUID> getReferences() {
        return references;
    }

    // TODO: DOCS
    public void set(TARDISMap map) {
        this.clear();
        this.references.clear();

        this.putAll(map);
        this.references.addAll(map.references);
    }

    public static class Serializer implements NBTSerializer<TARDISMap>, NBTDeserializer<TARDISMap> {

        private static final TARDIS.Serializer TARDIS_SERIALIZER = new TARDIS.Serializer();

        @Override
        public void serialize(CompoundNBT nbt, TARDISMap map) {
            ListNBT tardisNBTs = new ListNBT();
            map.forEach((id, tardis) -> tardisNBTs.add(TARDIS_SERIALIZER.serialize(tardis)));

            ListNBT referenceNBTs = new ListNBT();
            map.getReferences().forEach(uuid -> {
                CompoundNBT compound = new CompoundNBT();
                compound.putUUID("uuid", uuid);

                referenceNBTs.add(compound);
            });

            nbt.put("tardises", tardisNBTs);
            nbt.put("references", referenceNBTs);
        }

        @Override
        public TARDISMap deserialize(CompoundNBT nbt) {
            TARDISMap map = new TARDISMap();
            nbt.getList("tardises", Constants.NBT.TAG_COMPOUND)
                    .forEach(inbt -> map.put(TARDIS_SERIALIZER.deserialize((CompoundNBT) inbt)));
            nbt.getList("references", Constants.NBT.TAG_COMPOUND)
                    .forEach(inbt -> map.getReferences().add(((CompoundNBT) inbt).getUUID("uuid")));

            return map;
        }
    }
}
