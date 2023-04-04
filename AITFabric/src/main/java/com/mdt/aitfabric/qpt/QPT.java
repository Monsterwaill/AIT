package com.mdt.aitfabric.qpt;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

import java.util.Objects;
import java.util.UUID;

public class QPT {

    public final UUID qpt_id;

    public final UUID qpt_owner;

    public Interior interior;

    public Exterior exterior;

    public QPT(UUID qptID, UUID qptOwner) {
        // QPT means Quantum Phase Transporter
        qpt_id = qptID;
        qpt_owner = qptOwner;
        interior = new Interior(qpt_id);
        exterior = new Exterior(qpt_id);


    }

    public NbtCompound save(NbtCompound tag) {
        tag.putUuid("qpt_id", qpt_id);
        tag.putUuid("qpt_owner", qpt_owner);
        tag = interior.save(tag); // Saves the interior information
        tag = exterior.save(tag); // Saves the exterior information

        return tag;
    }
}


class Interior {
    public final UUID qpt_id;

    public BlockPos blockPos;

    public boolean hasGenerated = false;

    public void setAsGenerated() {
        this.hasGenerated = true;
    }

    public Interior(UUID qptID) {
        qpt_id = qptID;
    }

    public Interior(UUID qptID, NbtCompound tag) {
        qpt_id = qptID;
        NbtCompound interior_tag = (NbtCompound) tag.get("interior");
        assert interior_tag != null;


        blockPos = BlockPos.fromLong(interior_tag.getLong("block_pos"));
        hasGenerated = interior_tag.getBoolean("has_gen");
    }

    public NbtCompound save(NbtCompound tag) {
        NbtCompound interior_tag = new NbtCompound();
        interior_tag.putLong("block_pos", blockPos.asLong());
        interior_tag.putBoolean("has_gen", this.hasGenerated);
        tag.put("interior", tag);


        return tag;
    }
}

class Exterior {
    public final UUID qpt_id;

    public BlockPos blockPos;

    public Exterior(UUID qptID) {
        qpt_id = qptID;
    }

    public Exterior(UUID qptID, NbtCompound tag) {
        qpt_id = qptID;
        NbtCompound exterior_tag = (NbtCompound) tag.get("exterior");
        assert exterior_tag != null;


        blockPos = BlockPos.fromLong(exterior_tag.getLong("block_pos"));
    }

    public NbtCompound save(NbtCompound tag) {
        NbtCompound exterior_tag = new NbtCompound();
        exterior_tag.putLong("block_pos", blockPos.asLong());

        tag.put("exterior", tag);

        return tag;
    }


}
