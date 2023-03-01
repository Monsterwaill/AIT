package io.mdt.ait.tardis.exterior.impl;

import io.mdt.ait.tardis.portal.Portal3i;
import io.mdt.ait.tardis.exterior.TARDISExteriorSchema;
import io.mdt.ait.tardis.exterior.impl.model.BasicBoxModelExteriorSchema;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.Entity;

public class BasicBoxExteriorSchema extends TARDISExteriorSchema {

    public BasicBoxExteriorSchema() {
        super("basic", "exterior.ait.basic");
    }

    @Override
    public <T extends Entity> EntityModel<T> model() {
        return new BasicBoxModelExteriorSchema<>();
    }

    @Override
    public Portal3i portal() {
        return new Portal3i(1.275D, 2.5D, 0.5D, 1.269D, 0.22595D);
    }
}
