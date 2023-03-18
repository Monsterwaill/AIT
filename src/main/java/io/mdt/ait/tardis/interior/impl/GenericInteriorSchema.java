package io.mdt.ait.tardis.interior.impl;

import io.mdt.ait.tardis.interior.TARDISInteriorSchema;
import net.minecraft.util.ResourceLocation;

/** A generic interior, made for implementing datapack interiors. */
public class GenericInteriorSchema extends TARDISInteriorSchema {

    public GenericInteriorSchema(String id, String translation, ResourceLocation location) {
        super(id, translation, location);
    }
}
